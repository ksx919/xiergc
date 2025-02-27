package com.example.xiergc.mapper;

import com.example.xiergc.entity.Article;
import com.example.xiergc.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface UserMapper {
    //根据用户名查询用户
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    //添加用户
    @Insert("insert into user(username,password,name,reg_time)" +
            " values(#{username},#{password},#{name},now())")
    void add(String username, String password,String name);

    //更新用户最近一次登录时间
    @Update("update user set last_login_time=now() where username=#{username}")
    void updatelogintime(String username);

    //修改用户昵称
    @Update("update user set name=#{name} where username=#{username}")
    void updatename(String name,String username);

    //修改用户头像
    @Update("update user set avatar_url=#{avatarUrl} where id=#{id}")
    void updateAvatar(String avatarUrl,int id);

    //更改密码
    @Update("update user set password=#{s} where id=#{id}")
    void updatePwd(String s, int id);

    @Select("SELECT * FROM articles WHERE author_id = #{id} ORDER BY publish_date DESC")
    List<Article> GetCreated(int id);

    @Select("SELECT articles.* ," +
            "user.name AS author_name, " +
            "user.avatar_url AS author_avatar_url " +
            "FROM articles " +
            "INNER JOIN article_likes ON articles.id = article_likes.article_id " +
            "INNER JOIN user ON articles.author_id = user.id " +
            "WHERE article_likes.user_id = #{id} " +
            "ORDER BY articles.publish_date DESC")
    List<Article> GetLiked(int id);

    @Select("SELECT articles.*, " +
            "user.name AS author_name, " +
            "user.avatar_url AS author_avatar_url" +
            "FROM articles " +
            "INNER JOIN article_collections ON articles.id = article_collections.article_id " +
            "INNER JOIN user ON articles.author_id = user.id " +
            "WHERE article_collections.user_id = #{id} " +
            "ORDER BY articles.publish_date DESC")
    List<Article> GetCollected(int id);
}
