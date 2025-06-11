// app/src/main/java/com/example/tradeupsprojecy/data/models/SendMessageRequest.java
package com.example.tradeupsprojecy.data.models;

public class SendMessageRequest {
    private Long conversationId;
    private Long receiverId;
    private String message;
    private Long itemId;

    public SendMessageRequest() {}

    public SendMessageRequest(Long receiverId, String message) {
        this.receiverId = receiverId;
        this.message = message;
    }

    // Getters and Setters
    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
}