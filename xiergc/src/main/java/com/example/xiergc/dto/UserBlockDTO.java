package com.example.xiergc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserBlockDTO {
    @NotNull(message = "目标用户ID不能为空")
    @Min(1)
    private Long targetId;
}
