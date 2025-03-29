package com.example.xiergc.dto;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UserDTO {
    private String name;
    private String bio;

    @URL(message = "头像地址必须是合法的URL")
    private String avatarUrl;
}
