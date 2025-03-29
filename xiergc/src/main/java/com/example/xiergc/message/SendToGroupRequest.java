package com.example.xiergc.message;

import lombok.Data;

/**
 * 发送给群组的群聊消息的 Message
 */
@Data
public class SendToGroupRequest implements Message {

    public static final String TYPE = "SEND_TO_GROUP_REQUEST";

    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private String content;
    /**
     * 群组ID
     */
    private Long groupId;
}
