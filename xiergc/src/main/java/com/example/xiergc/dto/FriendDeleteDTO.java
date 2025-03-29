package com.example.xiergc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendDeleteDTO {
    @NotNull(message = "好友ID不能为空")
    @Min(1)
    private Long friendId;
}
