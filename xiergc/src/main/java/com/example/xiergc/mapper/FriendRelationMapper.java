package com.example.xiergc.mapper;

import com.example.xiergc.entity.FriendRelation;
import com.example.xiergc.entity.Result;
import com.example.xiergc.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendRelationMapper {
    void insert(FriendRelation relation);
    void deleteRelation(@Param("userId") Long userId,
                        @Param("friendId") Long friendId);
    boolean isFriends(@Param("user1") Long user1,
                      @Param("user2") Long user2);
    Result<List<UserDTO>> getFriendList(Long userId);
    boolean isBlocked(@Param("blockerId") Long blockerId, @Param("blockedId") Long blockedId);
}