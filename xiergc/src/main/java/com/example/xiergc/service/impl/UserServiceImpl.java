package com.example.xiergc.service.impl;

import com.example.xiergc.entity.User;
import com.example.xiergc.mapper.UserMapper;
import com.example.xiergc.service.UserService;
import com.example.xiergc.utils.MD5Utils;
import com.example.xiergc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByUserName(String username) {
        User u = userMapper.findByUserName(username);
        return u;
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
        userMapper.updatelogintime(username);
    }

    @Override
    public void updatename(String name) {
        Map<String,Object> map= ThreadLocalUtil.get();
        String username=(String) map.get("username");
        userMapper.updatename(name,username);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map= ThreadLocalUtil.get();
        int id=(int) map.get("id");
        userMapper.updateAvatar(avatarUrl,id);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map= ThreadLocalUtil.get();
        int id=(int) map.get("id");
        userMapper.updatePwd(MD5Utils.string2MD5(newPwd),id);
    }
}
