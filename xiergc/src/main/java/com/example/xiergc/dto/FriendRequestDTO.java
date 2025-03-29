package com.example.xiergc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendRequestDTO {
    @NotNull(message = "接收方ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long receiverId;
}
