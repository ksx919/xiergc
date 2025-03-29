package com.example.xiergc.mapper;

import com.example.xiergc.entity.NonFriendMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;

@Mapper
public interface NonFriendMessageMapper {
    void insert(NonFriendMessage message);
    NonFriendMessage findBySenderAndReceiver(@Param("senderId") Long senderId,
                                           @Param("receiverId") Long receiverId);
    void markAsReplied(@Param("senderId") Long senderId,
                      @Param("receiverId") Long receiverId,
                      @Param("repliedAt") LocalDateTime repliedAt);
} 