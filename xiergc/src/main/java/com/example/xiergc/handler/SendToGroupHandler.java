package com.example.xiergc.handler;

import com.example.xiergc.entity.ChatMessage;
import com.example.xiergc.entity.GroupMember;
import com.example.xiergc.mapper.GroupMemberMapper;
import com.example.xiergc.message.SendResponse;
import com.example.xiergc.message.SendToGroupRequest;
import com.example.xiergc.rabbitmq.MessageProducer;
import com.example.xiergc.utils.ThreadLocalUtil;
import com.example.xiergc.utils.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class SendToGroupHandler implements MessageHandler<SendToGroupRequest> {
    private static final Logger logger = LoggerFactory.getLogger(SendToGroupHandler.class);
    
    @Autowired
    private MessageProducer messageProducer;
    
    @Autowired
    private GroupMemberMapper groupMemberMapper;

    private Long getCurrentUserId() {
        Map<String, Object> map = ThreadLocalUtil.get();
        return ((Number) map.get("id")).longValue();
    }

    @Override
    public void execute(WebSocketSession session, SendToGroupRequest message) {
        logger.info("[execute][开始处理群聊消息: {}]", message);
        try {
            // 生成消息ID
            String msgId = UUID.randomUUID().toString();
            Long senderId = getCurrentUserId();
            Long groupId = message.getGroupId();

            // 创建群聊消息
            ChatMessage chatMessage = new ChatMessage(
                    msgId,
                    senderId,
                    groupId,
                    message.getContent(),
                    LocalDateTime.now(),
                    true
            );

            // 发送到消息队列
            messageProducer.sendMessage(chatMessage);

            // 立即返回发送成功
            SendResponse sendResponse = new SendResponse()
                    .setMsgId(msgId)
                    .setCode(0);
            WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);

            // 获取群组所有成员
            List<GroupMember> members = groupMemberMapper.findByGroupId(groupId);
            
            // 广播消息给群组所有在线成员（除了发送者自己）
            message.setMsgId(msgId); // 设置生成的消息ID
            for (GroupMember member : members) {
                if (!member.getUserId().equals(senderId)) { // 不发送给自己
                    try {
                        WebSocketUtil.send(member.getUserId().toString(), SendToGroupRequest.TYPE, message);
                    } catch (Exception e) {
                        // 用户可能离线，记录日志但不影响其他用户的消息发送
                        logger.debug("[execute][用户({}) 可能离线，消息发送失败: {}]", member.getUserId(), e.getMessage());
                    }
                }
            }
            logger.info("[execute][群聊消息处理完成]");
        } catch (Exception e) {
            logger.error("[execute][处理群聊消息时发生异常]", e);
            throw e;
        }
    }

    @Override
    public String getType() {
        return SendToGroupRequest.TYPE;
    }
}
