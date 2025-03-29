package com.example.xiergc.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.xiergc.entity.ChatMessage;

@Mapper
public interface ChatMessageMapper {
    void insert(ChatMessage message);
    List<ChatMessage> selectHistory(@Param("userId") Long userId,
                                    @Param("targetId") Long targetId,
                                    @Param("isGroup") boolean isGroup,
                                    @Param("startTime") LocalDateTime startTime);
}
