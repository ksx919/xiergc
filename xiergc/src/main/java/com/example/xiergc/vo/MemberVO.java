package com.example.xiergc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MemberVO {
    private Long userId;
    private String nickname;
    private String avatar;
    private String role;
    private LocalDateTime joinedAt;
}
