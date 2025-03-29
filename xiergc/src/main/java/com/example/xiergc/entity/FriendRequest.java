package com.example.xiergc.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendRequest {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String status;//ENUM('PENDING', 'ACCEPTED', 'REJECTED')
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
