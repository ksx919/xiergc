package com.example.xiergc.rabbitmq;

import com.example.xiergc.config.RabbitMQConfig;
import com.example.xiergc.entity.ChatMessage;
import com.example.xiergc.entity.MessageStatus;
import com.example.xiergc.mapper.MessageStatusMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MessageRetryHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageRetryHandler.class);
    
    private final RabbitTemplate rabbitTemplate;
    private final MessageStatusMapper messageStatusMapper;
    
    @Value("${rabbitmq.retry.max-attempts:3}")
    private int maxRetryAttempts;
    
    @Value("${rabbitmq.retry.initial-interval:1000}")
    private long retryInitialInterval;
    
    @Value("${rabbitmq.retry.multiplier:2}")
    private double retryMultiplier;

    public void handleRetry(ChatMessage message, Exception e) {
        MessageStatus status = messageStatusMapper.findByMessageId(message.getId());
        if (status == null) {
            logger.error("消息状态不存在: {}", message.getId());
            return;
        }

        int retryCount = status.getRetryCount() != null ? status.getRetryCount() : 0;
        if (retryCount >= maxRetryAttempts) {
            logger.error("消息重试次数超过最大限制: {}", message.getId());
            status.setStatus("FAILED");
            status.setUpdateTime(LocalDateTime.now());
            messageStatusMapper.updateStatus(status);
            return;
        }

        // 计算重试延迟时间
        long delay = (long) (retryInitialInterval * Math.pow(retryMultiplier, retryCount));
        
        try {
            // 更新重试次数
            status.setRetryCount(retryCount + 1);
            status.setUpdateTime(LocalDateTime.now());
            messageStatusMapper.updateStatus(status);

            // 延迟重试发送
            Thread.sleep(delay);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.MESSAGE_EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY,
                    message
            );
            
            logger.info("消息重试发送成功: {}, 重试次数: {}", message.getId(), retryCount + 1);
        } catch (Exception ex) {
            logger.error("消息重试发送失败: {}", ex.getMessage());
        }
    }
} 