package com.example.xiergc.service.impl;

import com.example.xiergc.entity.ChatMessage;
import com.example.xiergc.entity.User;
import com.example.xiergc.exception.BusinessException;
import com.example.xiergc.mapper.ChatMessageMapper;
import com.example.xiergc.mapper.FriendRelationMapper;
import com.example.xiergc.mapper.GroupMemberMapper;
import com.example.xiergc.mapper.UserMapper;
import com.example.xiergc.message.UserBlockedNoticeRequest;
import com.example.xiergc.rabbitmq.MessageProducer;
import com.example.xiergc.service.ChatMessageService;
import com.example.xiergc.utils.ThreadLocalUtil;
import com.example.xiergc.utils.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private FriendRelationMapper friendRelationMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private ChatMessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;

    private Long getCurrentUserId() {
        Map<String, Object> map = ThreadLocalUtil.get();
        return ((Number) map.get("id")).longValue();
    }

    @Override
    public void sendPrivateMessage(Long receiverId, String content) {
        Long senderId = getCurrentUserId();
        // 检查是否被屏蔽
        if (friendRelationMapper.isBlocked(receiverId, senderId)) {
            // 获取被屏蔽用户的信息
            User blockedUser = userMapper.findById(receiverId);
            if (blockedUser != null) {
                // 发送被屏蔽通知给发送者
                UserBlockedNoticeRequest notice = new UserBlockedNoticeRequest()
                    .setBlockedUserId(receiverId)
                    .setBlockedUserNickname(blockedUser.getName());
                WebSocketUtil.send(senderId.toString(), UserBlockedNoticeRequest.TYPE, notice);
            }
            throw new BusinessException("消息发送失败，已被对方屏蔽");
        }

        ChatMessage message = new ChatMessage(senderId,receiverId,content,LocalDateTime.now(),false);

        messageProducer.sendMessage(message);
    }

    @Override
    public void sendGroupMessage(Long groupId, String content) {
        Long senderId = getCurrentUserId();
        // 检查是否群成员
        if (!groupMemberMapper.existsInGroup(groupId, senderId)) {
            throw new BusinessException("非群成员无法发送消息");
        }

        ChatMessage message = new ChatMessage(senderId,groupId,content,LocalDateTime.now(),false);

        messageProducer.sendMessage(message);
    }

    @Override
    public List<ChatMessage> getHistory(Long targetId, boolean isGroup) {
        Long userId = getCurrentUserId();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return messageMapper.selectHistory(userId, targetId, isGroup, oneMonthAgo);
    }
}
