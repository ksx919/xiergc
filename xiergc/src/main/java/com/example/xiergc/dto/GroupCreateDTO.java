package com.example.xiergc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupCreateDTO {
    @NotBlank(message = "群名称不能为空")
    private String name;
}
