package com.example.xiergc.rabbitmq;

import com.example.xiergc.config.RabbitMQConfig;
import com.example.xiergc.entity.ChatMessage;
import com.example.xiergc.mapper.ChatMessageMapper;
import com.example.xiergc.mapper.FriendRelationMapper;
import com.example.xiergc.mapper.NonFriendMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final ChatMessageMapper messageMapper;
    private final FriendRelationMapper friendRelationMapper;
    private final NonFriendMessageMapper nonFriendMessageMapper;

    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE)
    public void saveMessage(ChatMessage message) {
        try {
            messageMapper.insert(message);
            
            // 检查是否是回复非好友消息
            if (!message.isGroup()) {
                boolean isFriends = friendRelationMapper.isFriends(message.getSenderId(), message.getReceiverId());
                if (!isFriends) {
                    // 查找并更新非好友消息记录
                    nonFriendMessageMapper.markAsReplied(
                        message.getReceiverId(), // 原发送者
                        message.getSenderId(),   // 原接收者
                        LocalDateTime.now()
                    );
                }
            }
        } catch (Exception e) {
            // 添加重试逻辑或死信队列处理
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}