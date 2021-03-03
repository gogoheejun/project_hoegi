package com.hjhj.daedan;

public class MessageItem {
    public String userId;
    public String name;
    public String msg;
    public String profileUrl;
    public String time;

    public MessageItem() {
    }

    public MessageItem(String userId, String name, String msg, String profileUrl, String time) {
        this.userId = userId;
        this.name = name;
        this.msg = msg;
        this.profileUrl = profileUrl;
        this.time = time;
    }
}
