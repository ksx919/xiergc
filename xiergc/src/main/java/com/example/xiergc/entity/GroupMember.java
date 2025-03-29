package com.example.xiergc.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMember {
    private Long groupId;
    private Long userId;
    private String role;//ENUM('MEMBER', 'ADMIN')
    private LocalDateTime joinedAt;
}
