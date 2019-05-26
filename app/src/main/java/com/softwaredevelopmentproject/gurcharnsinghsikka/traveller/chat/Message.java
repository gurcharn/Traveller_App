package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat;

public class Message {
    private String senderId;
    private String receiverId;
    private String content;
    private long time;

    Message(String senderId, String receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.time = System.currentTimeMillis();
    }

    Message(String senderId, String receiverId, String content, long time) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.time = time;
    }

    String getSenderId() {
        return senderId;
    }

    void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    String getRecieverId() {
        return receiverId;
    }

    void setRecieverId(String recieverId) {
        this.receiverId = recieverId;
    }

    String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    long getTime() {
        return time;
    }

    void setTime(long time) {
        this.time = time;
    }
}
