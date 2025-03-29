package com.example.xiergc.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.example.xiergc.config.RabbitMQConfig;
import com.example.xiergc.entity.ChatMessage;
import com.example.xiergc.entity.MessageStatus;
import com.example.xiergc.mapper.MessageStatusMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageProducer {
    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    private static final int REDIS_EXPIRE_DAYS = 30;
    
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final MessageStatusMapper messageStatusMapper;

    @Transactional
    public void sendMessage(ChatMessage message) {
        String messageId = UUID.randomUUID().toString();
        message.setId(messageId);
        
        try {
            // 记录消息发送状态
            MessageStatus status = new MessageStatus(messageId, "SENDING");
            messageStatusMapper.insert(status);
            
            // 存储到Redis（保留最近100条）
            String redisKey = message.isGroup() ?
                    "group:msg:" + message.getReceiverId() :
                    "private:msg:" + message.getSenderId() + ":" + message.getReceiverId();

            // 使用Redis事务确保原子性
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                connection.multi();
                connection.stringCommands().set(
                    redisKey.getBytes(),
                    JSON.toJSONString(message).getBytes()
                );
                connection.expire(redisKey.getBytes(), Duration.ofDays(REDIS_EXPIRE_DAYS).getSeconds());
                return connection.exec();
            });

            // 发送到RabbitMQ
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.MESSAGE_EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY,
                    message
            );
            
            // 更新消息状态为已发送
            status.setStatus("SENT");
            status.setUpdateTime(LocalDateTime.now());
            messageStatusMapper.updateStatus(status);
            
        } catch (Exception e) {
            logger.error("消息发送失败: {}", e.getMessage());
            MessageStatus status = new MessageStatus(messageId, "FAILED");
            status.setUpdateTime(LocalDateTime.now());
            messageStatusMapper.updateStatus(status);
            throw e;
        }
    }
}