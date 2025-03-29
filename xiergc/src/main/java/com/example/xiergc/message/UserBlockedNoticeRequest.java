package com.example.xiergc.message;

/**
 * 用户被屏蔽的通知 Message
 */
public class UserBlockedNoticeRequest implements Message {

    public static final String TYPE = "USER_BLOCKED_NOTICE_REQUEST";

    /**
     * 被屏蔽的用户ID
     */
    private Long blockedUserId;

    /**
     * 被屏蔽的用户昵称
     */
    private String blockedUserNickname;

    public Long getBlockedUserId() {
        return blockedUserId;
    }

    public UserBlockedNoticeRequest setBlockedUserId(Long blockedUserId) {
        this.blockedUserId = blockedUserId;
        return this;
    }

    public String getBlockedUserNickname() {
        return blockedUserNickname;
    }

    public UserBlockedNoticeRequest setBlockedUserNickname(String blockedUserNickname) {
        this.blockedUserNickname = blockedUserNickname;
        return this;
    }
} 