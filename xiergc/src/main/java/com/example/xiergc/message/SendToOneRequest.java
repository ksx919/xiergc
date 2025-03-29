package com.example.xiergc.message;

import lombok.Data;

/**
 * 发送给指定用户的私聊消息的 Message
 */
@Data
public class SendToOneRequest implements Message {

    public static final String TYPE = "SEND_TO_ONE_REQUEST";

    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private String content;
    /**
     * 接收者ID
     */
    private String toUser;

    private String msgType;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
