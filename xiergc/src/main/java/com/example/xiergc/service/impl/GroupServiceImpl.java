package com.example.xiergc.service.impl;

import com.example.xiergc.entity.ChatGroup;
import com.example.xiergc.entity.GroupMember;
import com.example.xiergc.entity.Result;
import com.example.xiergc.entity.UserSimpleInfo;
import com.example.xiergc.dto.GroupCreateDTO;
import com.example.xiergc.vo.GroupVO;
import com.example.xiergc.vo.MemberVO;
import com.example.xiergc.mapper.ChatGroupMapper;
import com.example.xiergc.mapper.GroupMemberMapper;
import com.example.xiergc.service.GroupService;
import com.example.xiergc.service.UserService;
import com.example.xiergc.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final ChatGroupMapper groupMapper;
    private final GroupMemberMapper memberMapper;
    private final UserService userService;

    private Long getCurrentUserId() {
        Map<String, Object> map = ThreadLocalUtil.get();
        return ((Number) map.get("id")).longValue();
    }

    @Override
    @Transactional
    public Result<GroupVO> createGroup(GroupCreateDTO dto) {
        Long ownerId = getCurrentUserId();
        ChatGroup group = new ChatGroup();
        group.setName(dto.getName());
        group.setOwnerId(ownerId);
        group.setCreatedAt(LocalDateTime.now());
        groupMapper.insert(group);

        addMemberToGroup(group.getId(), ownerId, "ADMIN");
        return Result.success(new GroupVO(group.getId(), group.getName(), ownerId, group.getCreatedAt()));
    }

    @Override
    public Result<List<MemberVO>> getMembers(Long groupId) {
        ChatGroup group = groupMapper.findById(groupId);
        if (group == null) return Result.error("群组不存在");

        List<GroupMember> members = memberMapper.findByGroupId(groupId);
        List<UserSimpleInfo> userInfos = userService.batchGetUserInfo(
                members.stream().map(GroupMember::getUserId).collect(Collectors.toList())
        );

        List<MemberVO> vos = members.stream()
                .map(m -> createMemberVO(m, userInfos))
                .collect(Collectors.toList());

        return Result.success(vos);
    }

    @Override
    @Transactional
    public Result<?> inviteMember(Long groupId, Long targetUserId) {
        Long operatorId = getCurrentUserId();
        if (!hasOperatePermission(groupId, operatorId)) {
            return Result.error("无操作权限");
        }

        if (userService.findById(targetUserId)==null) {
            return Result.error("用户不存在");
        }

        if (memberMapper.existsInGroup(groupId, targetUserId)) {
            return Result.error("用户已在群中");
        }

        addMemberToGroup(groupId, targetUserId, "MEMBER");
        return Result.success();
    }

    @Override
    @Transactional
    public Result<?> removeMember(Long groupId, Long targetUserId) {
        Long operatorId = getCurrentUserId();
        ChatGroup group = groupMapper.findById(groupId);
        if (group == null) return Result.error("群组不存在");

        if (!isAdminOrOwner(groupId, operatorId)) {
            return Result.error("无操作权限");
        }

        if (targetUserId.equals(group.getOwnerId())) {
            return Result.error("不能移除群主");
        }

        memberMapper.deleteByGroupAndUser(groupId, targetUserId);
        return Result.success();
    }

    @Override
    @Transactional
    public Result<?> leaveGroup(Long groupId) {
        Long userId = getCurrentUserId();
        ChatGroup group = groupMapper.findById(groupId);
        if (group == null) return Result.error("群组不存在");

        if (userId.equals(group.getOwnerId())) {
            handleOwnerExit(group);
        } else {
            memberMapper.deleteByGroupAndUser(groupId, userId);
        }
        return Result.success();
    }

    // 辅助方法
    private void addMemberToGroup(Long groupId, Long userId, String role) {
        GroupMember member = new GroupMember();
        member.setGroupId(groupId);
        member.setUserId(userId);
        member.setRole(role);
        member.setJoinedAt(LocalDateTime.now());
        memberMapper.insert(member);
    }

    private boolean hasOperatePermission(Long groupId, Long operatorId) {
        return memberMapper.findRoleByUser(groupId, operatorId)
                .map(role -> "ADMIN".equals(role) || isGroupOwner(groupId, operatorId))
                .orElse(false);
    }

    private boolean isGroupOwner(Long groupId, Long userId) {
        return userId.equals(groupMapper.findOwnerId(groupId));
    }

    private boolean isAdminOrOwner(Long groupId, Long userId) {
        return isGroupOwner(groupId, userId) ||
                memberMapper.findRoleByUser(groupId, userId)
                        .map("ADMIN"::equals)
                        .orElse(false);
    }

    private void handleOwnerExit(ChatGroup group) {
        Long currentUserId = getCurrentUserId();
        Long groupId = group.getId();

        // 获取所有非群主的管理员
        List<GroupMember> admins = memberMapper.findAdmins(groupId).stream()
                .filter(m -> !m.getUserId().equals(currentUserId))
                .collect(Collectors.toList());

        if (!admins.isEmpty()) {
            // 选择第一个非群主的管理员
            Long newOwner = admins.get(0).getUserId();

            // 先删除原群主（需要先于更新群主操作）
            memberMapper.deleteByGroupAndUser(groupId, currentUserId);

            // 更新群主
            groupMapper.updateOwner(groupId, newOwner);

            // 如果新群主原本不是ADMIN则更新（根据业务需求可选）
            memberMapper.updateRoleIfNeeded(groupId, newOwner, "ADMIN");
        } else {
            // 删除所有成员（包括自己）
            memberMapper.deleteByGroup(groupId);
            // 删除群组
            groupMapper.delete(groupId);
        }
    }

    private MemberVO createMemberVO(GroupMember member, List<UserSimpleInfo> userInfos) {
        UserSimpleInfo userInfo = userInfos.stream()
                .filter(u -> u.getId().equals(member.getUserId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("用户信息缺失"));

        return new MemberVO(
                member.getUserId(),
                userInfo.getName(),
                userInfo.getAvatar(),
                member.getRole(),
                member.getJoinedAt()
        );
    }
}
