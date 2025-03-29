package com.example.xiergc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.xiergc.message.AuthRequest;
import com.example.xiergc.message.Message;
import com.example.xiergc.utils.WebSocketUtil;
import com.example.xiergc.utils.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WebSocketHandler extends TextWebSocketHandler implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 消息类型与 MessageHandler 的映射\
     * 无需设置成静态变量
     */
    private final Map<String, MessageHandler> HANDLERS = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @Override //onopen
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("[afterConnectionEstablished][session({}) 接入]", session);
        
        // 从session的attributes中获取用户信息
        Map<String, Object> claims = (Map<String, Object>) session.getAttributes().get("claims");
        if (claims != null) {
            // 设置ThreadLocal
            ThreadLocalUtil.set(claims);
        }

        // 解析 jwtToken
        String jwtToken = (String) session.getAttributes().get("jwtToken");
        // 创建 AuthRequest 消息类型
        AuthRequest authRequest = new AuthRequest();
        authRequest.setJwtToken(jwtToken);
        // 获得消息处理器
        MessageHandler<AuthRequest> messageHandler = HANDLERS.get(AuthRequest.TYPE);
        if (messageHandler == null) {
            logger.error("[onOpen][认证消息类型，不存在消息处理器]");
            return;
        }
        messageHandler.execute(session, authRequest);
    }

    @Override // 对应 message 事件
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        logger.info("[handleMessage][session({}) 接收到一条消息({})]", session, textMessage); // 生产环境下，请设置成 debug 级别
        try {
            // 从session的attributes中获取用户信息并设置到ThreadLocal
            Map<String, Object> claims = (Map<String, Object>) session.getAttributes().get("claims");
            if (claims != null) {
                ThreadLocalUtil.set(claims);
            } else {
                logger.error("[handleMessage][session的claims为空]");
                return;
            }

            // 获得消息类型
            JSONObject jsonMessage = JSON.parseObject(textMessage.getPayload());
            String messageType = jsonMessage.getString("type");
            logger.info("[handleMessage][消息类型: {}]", messageType);
            
            // 获得消息处理器
            MessageHandler messageHandler = HANDLERS.get(messageType);
            if (messageHandler == null) {
                logger.error("[onMessage][消息类型({}) 不存在消息处理器]", messageType);
                return;
            }
            // 解析消息
            Class<? extends Message> messageClass = this.getMessageClass(messageHandler);
            // 处理消息
            Message messageObj = JSON.parseObject(jsonMessage.getString("body"), messageClass);
            logger.info("[handleMessage][开始处理消息: {}]", messageObj);
            messageHandler.execute(session, messageObj);
        } catch (Throwable throwable) {
            logger.error("[onMessage][session({}) message({}) 发生异常]", session, textMessage, throwable);
        }
    }

    @Override // 对应 close 事件
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("[afterConnectionClosed][session({}) 连接关闭。关闭原因是({})}]", session, status);
        WebSocketUtil.removeSession(session);
        // 清理ThreadLocal
        ThreadLocalUtil.remove();
    }

    @Override // 对应 error 事件
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.info("[handleTransportError][session({}) 发生异常]", session, exception);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 通过 ApplicationContext 获得所有 MessageHandler Bean
        applicationContext.getBeansOfType(MessageHandler.class).values() // 获得所有 MessageHandler Bean
                .forEach(messageHandler -> HANDLERS.put(messageHandler.getType(), messageHandler)); // 添加到 handlers 中
        logger.info("[afterPropertiesSet][消息处理器数量：{}]", HANDLERS.size());
    }

    private Class<? extends Message> getMessageClass(MessageHandler handler) {
        // 获得 Bean 对应的 Class 类名。因为有可能被 AOP 代理过。
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(handler);
        // 获得接口的 Type 数组
        Type[] interfaces = targetClass.getGenericInterfaces();
        Class<?> superclass = targetClass.getSuperclass();
        while ((Objects.isNull(interfaces) || 0 == interfaces.length) && Objects.nonNull(superclass)) { // 此处，是以父类的接口为准
            interfaces = superclass.getGenericInterfaces();
            superclass = targetClass.getSuperclass();
        }
        if (Objects.nonNull(interfaces)) {
            // 遍历 interfaces 数组
            for (Type type : interfaces) {
                // 要求 type 是泛型参数
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    // 要求是 MessageHandler 接口
                    if (Objects.equals(parameterizedType.getRawType(), MessageHandler.class)) {
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        // 取首个元素
                        if (Objects.nonNull(actualTypeArguments) && actualTypeArguments.length > 0) {
                            return (Class<Message>) actualTypeArguments[0];
                        } else {
                            throw new IllegalStateException(String.format("类型(%s) 获得不到消息类型", handler));
                        }
                    }
                }
            }
        }
        throw new IllegalStateException(String.format("类型(%s) 获得不到消息类型", handler));
    }
}
