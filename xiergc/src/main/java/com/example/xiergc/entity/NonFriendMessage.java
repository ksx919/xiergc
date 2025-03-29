package com.example.xiergc.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NonFriendMessage {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime sentAt;
    private boolean hasReplied;
    private LocalDateTime repliedAt;
} 