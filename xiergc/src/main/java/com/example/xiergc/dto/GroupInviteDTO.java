package com.example.xiergc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupInviteDTO {
    @NotNull(message = "被邀请用户ID不能为空")
    @Min(1)
    private Long targetUserId;
}
