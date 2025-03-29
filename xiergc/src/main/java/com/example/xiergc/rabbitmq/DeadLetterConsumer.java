package com.example.xiergc.rabbitmq;

import com.example.xiergc.config.RabbitMQConfig;
import com.example.xiergc.entity.ChatMessage;
import com.example.xiergc.entity.MessageStatus;
import com.example.xiergc.mapper.MessageStatusMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeadLetterConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DeadLetterConsumer.class);
    private final MessageStatusMapper messageStatusMapper;

    @RabbitListener(queues = "chat.message.dead.letter")
    public void handleDeadLetter(ChatMessage message) {
        try {
            logger.error("收到死信消息: {}", message);
            // 更新消息状态为失败
            MessageStatus status = new MessageStatus(message.getId(), "FAILED");
            messageStatusMapper.updateStatus(status);
            
            // 这里可以添加告警通知
            // 可以发送邮件、短信等通知管理员
            
        } catch (Exception e) {
            logger.error("处理死信消息失败: {}", e.getMessage());
        }
    }
} 