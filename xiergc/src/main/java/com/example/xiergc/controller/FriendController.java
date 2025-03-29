package com.example.xiergc.controller;

import com.example.xiergc.dto.*;
import com.example.xiergc.entity.Result;
import com.example.xiergc.vo.FriendRequestVO;
import com.example.xiergc.service.FriendService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    //发送好友请求
    @PostMapping("/requests")
    public Result<?> sendRequest(@RequestBody @Valid FriendRequestDTO dto) {
        return friendService.sendRequest(dto);
    }

    //获取收到的好友请求
    @GetMapping("/requests/query")
    public Result<List<FriendRequestVO>> getReceivedRequests(){
        return friendService.getReceivedRequests();
    }

    //处理好友请求
    @PutMapping("/requests/handle")
    public Result<?> handleRequest(@RequestBody @Valid FriendRequestHandleDTO dto) {
        return friendService.handleRequest(dto);
    }

    //删除好友
    @DeleteMapping("/delete")
    public Result<?> deleteFriend(@RequestBody @Valid FriendDeleteDTO dto) {
        return friendService.deleteFriend(dto);
    }

    // 屏蔽用户
    @PostMapping("/block")
    public Result<?> blockUser(@RequestBody @Valid UserBlockDTO dto) {
        return friendService.blockUser(dto);
    }

    //获取好友列表
    @GetMapping("/list")
    public Result<List<UserDTO>> getFriendList(){
        return friendService.getFriendList();
    }
}
