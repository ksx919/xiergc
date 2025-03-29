package com.example.xiergc.mapper;

import com.example.xiergc.entity.Article;
import com.example.xiergc.entity.User;
import com.example.xiergc.entity.UserSimpleInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    //根据用户名查询用户
    User findByUserName(String username);

    //添加用户
    void add(String username, String password, String name);

    //更新用户最近一次登录时间
    void updateLoginTime(String username);

    //修改用户昵称
    void updateName(String name, String username);

    //修改用户头像
    void updateAvatar(String avatarUrl, Long id);

    //更改密码
    void updatePassWord(String password, Long id);

    //获取以创建的文章
    List<Article> GetCreated(Long id);

    //获取点赞的文章
    List<Article> GetLiked(Long id);

    //获取收藏的文章
    List<Article> GetCollected(Long id);

    User findById(Long id);
    
    List<UserSimpleInfo> batchSelectUserSimpleInfo(List<Long> userIds);
}