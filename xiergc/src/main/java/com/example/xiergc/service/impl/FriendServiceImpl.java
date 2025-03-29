package com.example.xiergc.service.impl;

import com.example.xiergc.dto.*;
import com.example.xiergc.entity.FriendRelation;
import com.example.xiergc.entity.FriendRequest;
import com.example.xiergc.entity.Result;
import com.example.xiergc.vo.FriendRequestVO;
import com.example.xiergc.exception.BusinessException;
import com.example.xiergc.mapper.FriendRelationMapper;
import com.example.xiergc.mapper.FriendRequestMapper;
import com.example.xiergc.mapper.UserMapper;
import com.example.xiergc.service.FriendService;

import com.example.xiergc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendRequestMapper requestMapper;
    @Autowired
    private FriendRelationMapper relationMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Result<?> sendRequest(FriendRequestDTO dto) {
        try {
            Map<String,Object> map= ThreadLocalUtil.get();
            Long userId=((Number) map.get("id")).longValue();
            validateUsersExist(userId, dto.getReceiverId());

            if(userId.equals(dto.getReceiverId())){
                throw new BusinessException("不能向自己发起好友请求");
            }

            if (requestMapper.existsPendingRequest(userId, dto.getReceiverId())) {
                throw new BusinessException("已存在待处理请求");
            }

            if (relationMapper.isFriends(userId, dto.getReceiverId())) {
                throw new BusinessException("已经是好友关系");
            }

            FriendRequest request = new FriendRequest();
            request.setSenderId(userId);
            request.setReceiverId(dto.getReceiverId());
            request.setStatus("PENDING");
            request.setCreatedAt(LocalDateTime.now());
            requestMapper.insert(request);

            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<List<FriendRequestVO>> getReceivedRequests() {
        try {
            Map<String,Object> map= ThreadLocalUtil.get();
            Long userId=((Number) map.get("id")).longValue();
            List<FriendRequest> requests = requestMapper.findByReceiver(userId);
            List<FriendRequestVO> vos = requests.stream()
                    .map(r -> new FriendRequestVO(
                            r.getId(),
                            r.getSenderId(),
                            r.getStatus(),
                            r.getCreatedAt()))
                    .collect(Collectors.toList());
            return Result.success(vos);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<?> handleRequest(FriendRequestHandleDTO dto) {
        try {
            Map<String,Object> map= ThreadLocalUtil.get();
            Long userId=((Number) map.get("id")).longValue();
            FriendRequest request = requestMapper.findById(dto.getRequestId());
            if (request == null) {
                throw new BusinessException("好友请求不存在");
            }

            if (!request.getReceiverId().equals(userId)) {
                throw new BusinessException("无权操作该请求");
            }

            if ("ACCEPT".equalsIgnoreCase(dto.getAction())) {
                createFriendRelation(request.getSenderId(), request.getReceiverId());
                request.setStatus("ACCEPTED");
            } else if ("REJECT".equalsIgnoreCase(dto.getAction())) {
                request.setStatus("REJECTED");
            } else {
                throw new BusinessException("无效操作类型");
            }

            request.setUpdatedAt(LocalDateTime.now());
            requestMapper.update(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    private void createFriendRelation(Long user1, Long user2) {
        FriendRelation relation1 = new FriendRelation();
        relation1.setUserId(user1);
        relation1.setFriendId(user2);
        relation1.setRelationType("FRIEND");
        relation1.setCreatedAt(LocalDateTime.now());

        FriendRelation relation2 = new FriendRelation();
        relation2.setUserId(user2);
        relation2.setFriendId(user1);
        relation2.setRelationType("FRIEND");
        relation2.setCreatedAt(LocalDateTime.now());

        relationMapper.insert(relation1);
        relationMapper.insert(relation2);
    }

    @Override
    @Transactional
    public Result<?> deleteFriend(FriendDeleteDTO dto) {
        try {
            Map<String,Object> map= ThreadLocalUtil.get();
            Long userId=((Number) map.get("id")).longValue();
            relationMapper.deleteRelation(userId, dto.getFriendId());
            relationMapper.deleteRelation(dto.getFriendId(), userId);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<?> blockUser(UserBlockDTO dto) {
        try {
            Map<String,Object> map= ThreadLocalUtil.get();
            Long userId=((Number) map.get("id")).longValue();
            validateUsersExist(userId, dto.getTargetId());

            relationMapper.deleteRelation(userId, dto.getTargetId());

            FriendRelation blockRelation = new FriendRelation();
            blockRelation.setUserId(userId);
            blockRelation.setFriendId(dto.getTargetId());
            blockRelation.setRelationType("BLOCKED");
            blockRelation.setCreatedAt(LocalDateTime.now());
            relationMapper.insert(blockRelation);

            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    //获取所有好友
    @Override
    public Result<List<UserDTO>> getFriendList() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Long userId=((Number) map.get("id")).longValue();
        return relationMapper.getFriendList(userId);
    }

    private void validateUsersExist(Long... userIds) {
        for (Long userId : userIds) {
            if (userMapper.findById(userId) == null) {
                throw new RuntimeException("用户不存在: " + userId);
            }
        }
    }
}
