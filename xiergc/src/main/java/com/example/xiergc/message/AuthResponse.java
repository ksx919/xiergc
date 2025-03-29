package com.example.xiergc.message;

import lombok.Data;

/**
 * 用户认证响应
 */
@Data
public class AuthResponse implements Message {

    public static final String TYPE = "AUTH_RESPONSE";

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应提示
     */
    private String message;

    private String nickname;

    private String avatar;

    public AuthResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public AuthResponse(Integer code) {
        this.code = code;
    }
}
