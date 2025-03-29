package com.example.xiergc.service;

import com.example.xiergc.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    public void sendPrivateMessage(Long receiverId, String content);

    public void sendGroupMessage(Long groupId, String content);

    public List<ChatMessage> getHistory(Long targetId, boolean isGroup);
}
