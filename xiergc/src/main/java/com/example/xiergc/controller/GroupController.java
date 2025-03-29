package com.example.xiergc.controller;

import com.example.xiergc.entity.Result;
import com.example.xiergc.dto.GroupCreateDTO;
import com.example.xiergc.dto.GroupInviteDTO;
import com.example.xiergc.vo.GroupVO;
import com.example.xiergc.vo.MemberVO;
import com.example.xiergc.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public Result<GroupVO> createGroup(@RequestBody @Valid GroupCreateDTO dto) {
        return groupService.createGroup(dto);
    }

    @GetMapping("/{groupId}/members")
    public Result<List<MemberVO>> getMembers(@PathVariable Long groupId) {
        return groupService.getMembers(groupId);
    }

    @PostMapping("/{groupId}/invite")
    public Result<?> inviteMember(@PathVariable Long groupId,
                                  @RequestBody @Valid GroupInviteDTO dto) {
        return groupService.inviteMember(groupId, dto.getTargetUserId());
    }

    @DeleteMapping("/{groupId}/members/{targetUserId}")
    public Result<?> removeMember(@PathVariable Long groupId,
                                  @PathVariable Long targetUserId) {
        return groupService.removeMember(groupId, targetUserId);
    }

    @DeleteMapping("/{groupId}/exit")
    public Result<?> leaveGroup(@PathVariable Long groupId) {
        return groupService.leaveGroup(groupId);
    }
}
