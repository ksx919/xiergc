package com.example.xiergc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FriendRequestVO {
    private Long id;
    private Long senderId;
    private String status;
    private LocalDateTime createdAt;
}
