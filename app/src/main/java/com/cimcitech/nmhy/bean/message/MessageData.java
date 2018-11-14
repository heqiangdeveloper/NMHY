package com.cimcitech.nmhy.bean.message;

/**
 * Created by qianghe on 2018/7/4.
 */

public class MessageData {
    private int id;
    private int opened;//状态：0未读，1已读
    private String title;
    private String time;
    private MessageContent messageContent;

    public MessageData(int opened, String title, String time, MessageContent messageContent) {
        this.opened = opened;
        this.title = title;
        this.time = time;
        this.messageContent = messageContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpened() {
        return opened;
    }

    public void setOpened(int opened) {
        this.opened = opened;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(MessageContent messageContent) {
        this.messageContent = messageContent;
    }
}
