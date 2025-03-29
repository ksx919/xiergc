package com.example.xiergc.mapper;

import com.example.xiergc.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GroupMemberMapper {
    void insert(GroupMember member);
    List<GroupMember> findByGroupId(Long groupId);
    boolean existsInGroup(@Param("groupId") Long groupId, @Param("userId") Long userId);
    void deleteByGroupAndUser(@Param("groupId") Long groupId, @Param("userId") Long userId);
    void deleteByGroup(Long groupId);
    List<GroupMember> findAdmins(Long groupId);
    Optional<String> findRoleByUser(@Param("groupId") Long groupId, @Param("userId") Long userId);
    void updateRoleIfNeeded(@Param("groupId") Long groupId,
                            @Param("userId") Long userId,
                            @Param("role") String role);
}
