package com.example.xiergc.mapper;

import com.example.xiergc.entity.FriendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendRequestMapper {
    void insert(FriendRequest request);
    List<FriendRequest> findByReceiver(@Param("receiverId") Long receiverId);
    boolean existsPendingRequest(@Param("senderId") Long senderId,
                                 @Param("receiverId") Long receiverId);
    void update(FriendRequest request);
    FriendRequest findById(Long requestId);
}
