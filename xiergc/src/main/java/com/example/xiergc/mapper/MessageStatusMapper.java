package com.example.xiergc.mapper;

import com.example.xiergc.entity.MessageStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageStatusMapper {
    void insert(MessageStatus messageStatus);
    void updateStatus(MessageStatus messageStatus);
    MessageStatus findByMessageId(String messageId);
} 