package com.example.xiergc.service;

import com.example.xiergc.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public interface UserService {
    //根据用户名查询用户
    User findByUserName(String username);

    //注册
    void register(User user);

    //更新登录时间
    void updatelogintime(String username);

    //修改用户名
    void updatename(String name);

    //修改头像
    void updateAvatar(String avatarUrl);

    //更改密码
    void updatePwd(String newPwd);
}
