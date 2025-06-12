// app/src/main/java/com/example/tradeupsprojecy/data/models/Message.java
package com.example.tradeupsprojecy.data.models;

import java.util.Date;

public class Message {
    private Long id;
    private Long conversationId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Date timestamp;
    private boolean isRead;
    private String senderName;
    private String messageType;

    public Message() {}

    public Message(String content, Long senderId) {
        this.content = content;
        this.senderId = senderId;
        this.messageType = "TEXT";
        this.timestamp = new Date();
    }

    public Message(String content, String senderId) {
        this.content = content;
        this.setSenderId(senderId);
        this.messageType = "TEXT";
        this.timestamp = new Date();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    // String compatibility methods
    public void setSenderId(String senderId) {
        if (senderId != null) {
            try {
                this.senderId = Long.parseLong(senderId);
            } catch (NumberFormatException e) {
                this.senderId = null;
            }
        }
    }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
}