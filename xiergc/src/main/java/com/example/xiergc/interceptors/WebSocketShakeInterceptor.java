package com.example.xiergc.interceptors;

import com.example.xiergc.entity.User;
import com.example.xiergc.mapper.UserMapper;
import com.example.xiergc.utils.JwtUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义 HttpSessionHandshakeInterceptor 拦截器
 *
 * 因为 WebSocketSession 无法获得 ws 地址上的请求参数，所以只好通过该拦截器，获得 jwtToken 请求参数，设置到 attributes 中
 */
@Component
public class WebSocketShakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Autowired
    private UserMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(WebSocketShakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 从请求参数中获取jwtToken
        if(request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            String jwtToken = serverRequest.getServletRequest().getParameter("jwtToken");
            if (jwtToken == null || jwtToken.isEmpty()) {
                logger.error("WebSocket握手失败: JWT Token为空");
                return false;
            }
            
            try {
                // 解析JWT Token
                Map<String, Object> claims = JwtUtil.parseToken(jwtToken);
                Long userId = ((Number)claims.get("id")).longValue();
                
                // 获取用户信息
                User user = userMapper.findById(userId);
                if (user == null) {
                    logger.error("WebSocket握手失败: 用户不存在");
                    return false;
                }

                // 设置用户信息到attributes
                attributes.put("jwtToken", jwtToken);
                attributes.put("userId", userId);
                attributes.put("claims", claims);
                attributes.put("nickname", user.getName());
                attributes.put("avatar", user.getAvatarUrl());

            } catch (Exception e) {
                logger.error("WebSocket握手失败: {}", e.getMessage());
                return false;
            }
        }

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}