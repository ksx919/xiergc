package com.example.xiergc.message;

import lombok.Data;

/**
 * 用户认证请求
 */
@Data
public class AuthRequest implements Message {

    public static final String TYPE = "AUTH_REQUEST";

    /**
     * 认证 Token
     */
    private String jwtToken;
}