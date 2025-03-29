package com.example.xiergc.service;

import com.example.xiergc.dto.*;
import com.example.xiergc.entity.Result;
import com.example.xiergc.vo.FriendRequestVO;
import jakarta.validation.Valid;

import java.util.List;

public interface FriendService {
    Result<?> sendRequest(@Valid FriendRequestDTO dto);

    Result<List<FriendRequestVO>> getReceivedRequests();


    Result<?> handleRequest(@Valid FriendRequestHandleDTO dto);

    Result<?> deleteFriend(@Valid FriendDeleteDTO dto);

    Result<?> blockUser(@Valid UserBlockDTO dto);

    Result<List<UserDTO>> getFriendList();
}
