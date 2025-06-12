// app/src/main/java/com/example/tradeupsprojecy/data/models/entities/Message.java
package com.example.tradeupsprojecy.data.entities;

import java.util.Date;

public class Message {
    private Long id;
    private String conversationId;
    private String senderId;
    private String content;
    private Date createdAt;
    private boolean isRead;

    public Message() {}

    public Message(Long id, String conversationId, String senderId, String content, Date createdAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt;
        this.isRead = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}