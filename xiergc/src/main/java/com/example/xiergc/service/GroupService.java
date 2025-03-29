package com.example.xiergc.service;

import com.example.xiergc.entity.Result;
import com.example.xiergc.dto.GroupCreateDTO;
import com.example.xiergc.vo.GroupVO;
import com.example.xiergc.vo.MemberVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface GroupService {
    Result<GroupVO> createGroup(@Valid GroupCreateDTO dto);

    Result<List<MemberVO>> getMembers(Long groupId);

    Result<?> inviteMember(Long groupId, @NotNull(message = "被邀请用户ID不能为空") @Min(1) Long targetUserId);

    Result<?> removeMember(Long groupId, Long targetUserId);

    Result<?> leaveGroup(Long groupId);
}
