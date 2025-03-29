package com.example.xiergc.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatGroup {
    private Long id;
    private String name;
    private Long ownerId;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
