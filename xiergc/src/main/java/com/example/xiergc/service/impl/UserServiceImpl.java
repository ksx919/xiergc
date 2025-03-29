package com.example.xiergc.service.impl;

import com.example.xiergc.entity.Article;
import com.example.xiergc.entity.User;
import com.example.xiergc.entity.UserSimpleInfo;
import com.example.xiergc.mapper.UserMapper;
import com.example.xiergc.service.UserService;
import com.example.xiergc.utils.MD5Utils;
import com.example.xiergc.utils.ThreadLocalUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.xiergc.entity.RedisConstants.CACHE_USER_KEY;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public void register(User user) {
        //加密
        String md5String = MD5Utils.string2MD5(user.getPassword());
        //添加
        userMapper.add(user.getUsername(),md5String,user.getName());
    }

    @Override
    public void updatelogintime(String username){
        userMapper.updateLoginTime(username);
    }

    @Override
    public void updatename(String name) {
        Map<String,Object> map= ThreadLocalUtil.get();
        String username=(String) map.get("username");
        userMapper.updateName(name,username);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Long id=((Number) map.get("id")).longValue();
        userMapper.updateAvatar(avatarUrl,id);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Long id=((Number) map.get("id")).longValue();
        userMapper.updatePassWord(MD5Utils.string2MD5(newPwd),id);
    }

    @Override
    public List<Article> GetCreated(Long id) {
        List<Article> articles = userMapper.GetCreated(id);
        return articles;
    }

    @Override
    public List<Article> Getliked(Long id) {
        List<Article> articles = userMapper.GetLiked(id);
        return articles;
    }

    @Override
    public List<Article> GetCollected(Long id) {
        List<Article> articles = userMapper.GetCollected(id);
        return articles;
    }

    @Override
    public User findById(Long id) {
        String KEY = CACHE_USER_KEY + id;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String userJson = operations.get(KEY);
        if(userJson != null) {
            try {
                return objectMapper.readValue(userJson, User.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        User u = userMapper.findById(id);
        if(u == null) {
            return null;
        }
        try {
            operations.set(KEY, objectMapper.writeValueAsString(u));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return u;
    }

    @Override
    public List<UserSimpleInfo> batchGetUserInfo(List<Long> userIds) {
        return userMapper.batchSelectUserSimpleInfo(userIds);
    }
}
