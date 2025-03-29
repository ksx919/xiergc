package com.example.xiergc.handler;

import com.example.xiergc.entity.User;
import com.example.xiergc.mapper.UserMapper;
import com.example.xiergc.message.AuthRequest;
import com.example.xiergc.message.AuthResponse;
import com.example.xiergc.message.UserJoinNoticeRequest;
import com.example.xiergc.utils.ThreadLocalUtil;
import com.example.xiergc.utils.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Component
public class AuthMessageHandler implements MessageHandler<AuthRequest> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void execute(WebSocketSession session, AuthRequest message) {
        // 如果未传递 jwtToken
        if (message.getJwtToken().isEmpty()) {
            WebSocketUtil.send(session, AuthResponse.TYPE,
                    new AuthResponse(1,"认证 jwtToken 未传入"));
            return;
        }

        // 从session的attributes中获取用户信息
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) {
            WebSocketUtil.send(session, AuthResponse.TYPE,
                    new AuthResponse(1,"未查询到用户信息"));
            return;
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            WebSocketUtil.send(session, AuthResponse.TYPE,
                    new AuthResponse(1,"未查询到用户信息"));
            return;
        }

        // 设置ThreadLocal
        Map<String, Object> claims = (Map<String, Object>) session.getAttributes().get("claims");
        if (claims != null) {
            ThreadLocalUtil.set(claims);
        }

        // 添加到 WebSocketUtil 中
        WebSocketUtil.addSession(session, user.getName());
        WebSocketUtil.addSession(session, userId.toString());

        // 发送认证成功响应
        AuthResponse response = new AuthResponse(0);
        response.setNickname(user.getName());
        response.setAvatar(user.getAvatarUrl());
        WebSocketUtil.send(session, AuthResponse.TYPE, response);

        // 通知所有人，某个人加入了
        WebSocketUtil.broadcast(UserJoinNoticeRequest.TYPE,
                new UserJoinNoticeRequest().setNickname(user.getName()));
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }
}