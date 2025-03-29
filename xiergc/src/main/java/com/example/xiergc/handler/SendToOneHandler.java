package com.example.xiergc.handler;

import com.example.xiergc.entity.ChatMessage;
import com.example.xiergc.entity.User;
import com.example.xiergc.entity.NonFriendMessage;
import com.example.xiergc.message.UserBlockedNoticeRequest;
import com.example.xiergc.mapper.FriendRelationMapper;
import com.example.xiergc.mapper.UserMapper;
import com.example.xiergc.mapper.NonFriendMessageMapper;
import com.example.xiergc.message.SendResponse;
import com.example.xiergc.message.SendToOneRequest;
import com.example.xiergc.rabbitmq.MessageProducer;
import com.example.xiergc.utils.ThreadLocalUtil;
import com.example.xiergc.utils.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class SendToOneHandler implements MessageHandler<SendToOneRequest> {
    private static final Logger logger = LoggerFactory.getLogger(SendToOneHandler.class);
    
    @Autowired
    private MessageProducer messageProducer;
    
    @Autowired
    private FriendRelationMapper friendRelationMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private NonFriendMessageMapper nonFriendMessageMapper;

    private Long getCurrentUserId() {
        Map<String, Object> map = ThreadLocalUtil.get();
        logger.info("[getCurrentUserId][ThreadLocal map: {}]", map);
        if (map == null) {
            logger.error("[getCurrentUserId][ThreadLocal map is null]");
            throw new IllegalStateException("ThreadLocal map is null");
        }
        return ((Number) map.get("id")).longValue();
    }

    @Override
    public void execute(WebSocketSession session, SendToOneRequest message) {
        logger.info("[execute][开始处理私聊消息: {}]", message);
        try {
            // 生成消息ID
            String msgId = UUID.randomUUID().toString();
            Long senderId = getCurrentUserId();
            Long receiverId = Long.parseLong(message.getToUser());

            logger.info("[execute][发送者ID: {}, 接收者ID: {}]", senderId, receiverId);

            // 检查是否被屏蔽
            if (friendRelationMapper.isBlocked(receiverId, senderId)) {
                // 获取被屏蔽用户的信息
                User blockedUser = userMapper.findById(receiverId);
                if (blockedUser != null) {
                    // 发送被屏蔽通知给发送者
                    UserBlockedNoticeRequest notice = new UserBlockedNoticeRequest()
                        .setBlockedUserId(receiverId)
                        .setBlockedUserNickname(blockedUser.getName());
                    WebSocketUtil.send(senderId.toString(), UserBlockedNoticeRequest.TYPE, notice);
                }
                // 返回发送失败响应
                SendResponse sendResponse = new SendResponse()
                    .setMsgId(msgId)
                    .setCode(1)
                    .setMessage("消息发送失败，已被对方屏蔽");
                WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);
                return;
            }

            // 检查是否是好友关系
            boolean isFriends = friendRelationMapper.isFriends(senderId, receiverId);
            if (!isFriends) {
                // 检查是否已经发送过消息
                NonFriendMessage nonFriendMessage = nonFriendMessageMapper.findBySenderAndReceiver(senderId, receiverId);
                if (nonFriendMessage != null && !nonFriendMessage.isHasReplied()) {
                    // 已经发送过消息且对方未回复
                    SendResponse sendResponse = new SendResponse()
                        .setMsgId(msgId)
                        .setCode(2)
                        .setMessage("消息发送失败，请等待对方回复后再发送消息");
                    WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);
                    return;
                }
            }


            // 创建聊天消息
            ChatMessage chatMessage = new ChatMessage(
                    msgId,
                    senderId,
                    receiverId,
                    message.getContent(),
                    LocalDateTime.now(),
                    false
            );
            // 发送到消息队列
            messageProducer.sendMessage(chatMessage);

            // 如果不是好友关系，记录非好友消息
            if (!isFriends) {
                NonFriendMessage nonFriendMessage = new NonFriendMessage();
                nonFriendMessage.setSenderId(senderId);
                nonFriendMessage.setReceiverId(receiverId);
                nonFriendMessage.setSentAt(LocalDateTime.now());
                nonFriendMessage.setHasReplied(false);
                nonFriendMessageMapper.insert(nonFriendMessage);
            }

            // 立即返回发送成功
            SendResponse sendResponse = new SendResponse()
                    .setMsgId(msgId)
                    .setCode(0);
            WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);

            // 如果接收者在线，直接发送消息
            message.setMsgId(msgId); // 设置生成的消息ID
            try {
                WebSocketUtil.send(message.getToUser(), SendToOneRequest.TYPE, message);
            } catch (Exception e) {
                // 用户可能离线，记录日志但不影响消息发送流程
                logger.debug("[execute][接收者({}) 可能离线，消息发送失败: {}]", message.getToUser(), e.getMessage());
            }
            
            logger.info("[execute][私聊消息处理完成]");
        } catch (Exception e) {
            logger.error("[execute][处理私聊消息时发生异常]", e);
            throw e;
        }
    }

    @Override
    public String getType() {
        return SendToOneRequest.TYPE;
    }
}
