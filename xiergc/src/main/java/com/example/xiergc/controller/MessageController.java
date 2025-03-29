package com.example.xiergc.controller;

import com.example.xiergc.entity.ChatMessage;
import com.example.xiergc.entity.Result;
import com.example.xiergc.dto.HistoryQueryDTO;
import com.example.xiergc.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping("/history")
    public Result<List<ChatMessage>> getHistory(@RequestBody @Valid HistoryQueryDTO dto) {
        return Result.success(chatMessageService.getHistory(
                dto.getTargetId(),
                dto.isGroup()
        ));
    }
}
