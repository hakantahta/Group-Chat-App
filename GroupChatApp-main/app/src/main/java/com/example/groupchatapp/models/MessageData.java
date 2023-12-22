package com.example.groupchatapp.models;

public class MessageData {
    private String userId,messageName,messageContent;

    public MessageData(String userId, String messageName, String messageContent) {
        this.userId = userId;
        this.messageName = messageName;
        this.messageContent = messageContent;
    }

    public MessageData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}

