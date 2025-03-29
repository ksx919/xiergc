package com.example.xiergc.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoryQueryDTO {
    @NotNull
    private Long targetId;
    private boolean isGroup;
}
