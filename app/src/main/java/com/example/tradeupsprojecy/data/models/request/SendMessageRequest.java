// app/src/main/java/com/example/tradeupsprojecy/data/models/request/SendMessageRequest.java
package com.example.tradeupsprojecy.data.models.request;

public class SendMessageRequest {
    private String conversationId;
    private String senderId;
    private String content;

    public SendMessageRequest() {}

    // Getters and Setters
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}