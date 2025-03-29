package com.example.xiergc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RabbitMQConfig {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);
    
    public static final String MESSAGE_QUEUE = "chat.message.queue";
    public static final String MESSAGE_EXCHANGE = "chat.message.exchange";
    public static final String ROUTING_KEY = "message.save";
    
    @Value("${rabbitmq.message.ttl:86400000}")
    private long messageTtl;
    
    @Value("${rabbitmq.queue.max-length:100000}")
    private int maxQueueLength;
    
    @Value("${rabbitmq.retry.max-attempts:3}")
    private int maxRetryAttempts;
    
    @Value("${rabbitmq.retry.initial-interval:1000}")
    private long retryInitialInterval;
    
    @Value("${rabbitmq.retry.multiplier:2}")
    private double retryMultiplier;
    
    @Bean
    public MessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        List<String> allowedListPatterns = Arrays.asList(
            "java.util.*",
            "java.lang.*",
            "java.time.*",
            "com.example.xiergc.entity.*"
        );
        converter.setAllowedListPatterns(allowedListPatterns);
        return converter;
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        
        // 设置消息确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                logger.error("消息发送失败: {}", cause);
                // 这里可以添加重试逻辑
            }
        });
        
        // 设置消息返回回调
        rabbitTemplate.setReturnsCallback(returned -> {
            logger.error("消息路由失败: {}", returned.getMessage());
        });
        
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue mainMessageQueue(RabbitAdmin rabbitAdmin) {
        Queue queue = QueueBuilder.durable(MESSAGE_QUEUE)
                .withArgument("x-message-ttl", messageTtl)
                .withArgument("x-max-length", maxQueueLength)
                .withArgument("x-overflow", "reject-publish")
                .withArgument("x-dead-letter-exchange", "chat.message.dead.letter")
                .withArgument("x-dead-letter-routing-key", "chat.message.dead.letter")
                .build();
        
        try {
            rabbitAdmin.declareQueue(queue);
            logger.info("主消息队列创建成功: {}", MESSAGE_QUEUE);
        } catch (Exception e) {
            logger.debug("主消息队列已存在: {}", e.getMessage());
        }
        return queue;
    }

    @Bean
    public TopicExchange mainMessageExchange(RabbitAdmin rabbitAdmin) {
        TopicExchange exchange = new TopicExchange(MESSAGE_EXCHANGE);
        try {
            rabbitAdmin.declareExchange(exchange);
            logger.info("主交换机创建成功: {}", MESSAGE_EXCHANGE);
        } catch (Exception e) {
            logger.debug("主交换机已存在: {}", e.getMessage());
        }
        return exchange;
    }

    @Bean
    public Binding mainMessageBinding(Queue mainMessageQueue, TopicExchange mainMessageExchange, RabbitAdmin rabbitAdmin) {
        Binding binding = BindingBuilder.bind(mainMessageQueue)
                .to(mainMessageExchange)
                .with(ROUTING_KEY);
        try {
            rabbitAdmin.declareBinding(binding);
            logger.info("主绑定关系创建成功");
        } catch (Exception e) {
            logger.debug("主绑定关系已存在: {}", e.getMessage());
        }
        return binding;
    }

    @Bean
    public Queue deadLetterQueue(RabbitAdmin rabbitAdmin) {
        Queue queue = QueueBuilder.durable("chat.message.dead.letter")
                .withArgument("x-dead-letter-exchange", MESSAGE_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY)
                .build();
        
        try {
            rabbitAdmin.declareQueue(queue);
            logger.info("死信队列创建成功: chat.message.dead.letter");
        } catch (Exception e) {
            logger.debug("死信队列已存在: {}", e.getMessage());
        }
        return queue;
    }

    @Bean
    public TopicExchange deadLetterExchange(RabbitAdmin rabbitAdmin) {
        TopicExchange exchange = new TopicExchange("chat.message.dead.letter");
        try {
            rabbitAdmin.declareExchange(exchange);
            logger.info("死信交换机创建成功: chat.message.dead.letter");
        } catch (Exception e) {
            logger.debug("死信交换机已存在: {}", e.getMessage());
        }
        return exchange;
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange deadLetterExchange, RabbitAdmin rabbitAdmin) {
        Binding binding = BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with("chat.message.dead.letter");
        try {
            rabbitAdmin.declareBinding(binding);
            logger.info("死信绑定关系创建成功");
        } catch (Exception e) {
            logger.debug("死信绑定关系已存在: {}", e.getMessage());
        }
        return binding;
    }
}