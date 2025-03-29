package com.example.xiergc.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String messageType;
    private LocalDateTime sentAt;
    private boolean isGroup;
    private String status;
    private String senderIp;
    private String senderLocation;

    public ChatMessage() {
    }

    public ChatMessage(Long senderId, Long receiverId, String content, LocalDateTime sentAt, boolean isGroup) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentAt = sentAt;
        this.isGroup = isGroup;
    }

    public ChatMessage(String id, Long senderId, Long receiverId, String content, LocalDateTime sentAt, boolean isGroup) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentAt = sentAt;
        this.isGroup = isGroup;
    }
}
