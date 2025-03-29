package com.example.xiergc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendRequestHandleDTO {
    @NotNull(message = "请求ID不能为空")
    @Min(1)
    private Long requestId;

    @NotBlank(message = "操作类型不能为空")
    private String action;
}
