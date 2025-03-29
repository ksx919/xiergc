package com.example.xiergc.mapper;

import com.example.xiergc.entity.ChatGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatGroupMapper {
    void insert(ChatGroup group);
    ChatGroup findById(Long groupId);
    Long findOwnerId(Long groupId);
    void updateOwner(@Param("groupId") Long groupId, @Param("newOwnerId") Long newOwnerId);
    void delete(Long groupId);
}
