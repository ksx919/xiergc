package com.example.xiergc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GroupVO {
    private Long id;
    private String name;
    private Long ownerId;
    private LocalDateTime createdAt;
}
