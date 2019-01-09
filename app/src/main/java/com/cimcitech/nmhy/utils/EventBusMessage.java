package com.cimcitech.nmhy.utils;

/**
 * Created by qianghe on 2019/1/8.
 */

public class EventBusMessage {
    private String message;

    public EventBusMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
