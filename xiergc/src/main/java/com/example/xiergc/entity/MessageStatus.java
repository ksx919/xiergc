package com.example.xiergc.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageStatus {
    private String messageId;
    private String status; // SENDING, SENT, FAILED
    private Integer retryCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public MessageStatus(String messageId, String status) {
        this.messageId = messageId;
        this.status = status;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.retryCount = 0;
    }
} 