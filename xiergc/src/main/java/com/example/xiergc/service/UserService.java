package com.example.xiergc.service;

import com.example.xiergc.entity.Article;
import com.example.xiergc.entity.User;
import com.example.xiergc.entity.UserSimpleInfo;

import java.util.List;


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

    //获取用户创建的文章
    List<Article> GetCreated(Long id);

    List<Article> Getliked(Long id);

    List<Article> GetCollected(Long id);

    User findById(Long id);

    List<UserSimpleInfo> batchGetUserInfo(List<Long> userIds);
}
