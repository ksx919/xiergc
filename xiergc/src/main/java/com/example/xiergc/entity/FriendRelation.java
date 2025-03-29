package com.example.xiergc.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendRelation {
    private Long id;
    private Long userId;
    private Long friendId;
    private String relationType;//ENUM('FRIEND', 'BLOCKED')
    private LocalDateTime createdAt;
}
