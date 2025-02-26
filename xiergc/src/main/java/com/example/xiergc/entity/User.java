package com.example.xiergc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class User {
    private int id;
    @NotNull
    @Size(min = 5, max = 16, message = "账号长度应在 6 到 16 之间")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度应在 6 到 16 之间")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "密码必须包含字母和数字")
    private String password;

    private String name;
    private String bio;

    @URL(message = "头像地址必须是合法的URL")
    private String avatar;
    private LocalDateTime regTime;
    private LocalDateTime lastLoginTime;
}
