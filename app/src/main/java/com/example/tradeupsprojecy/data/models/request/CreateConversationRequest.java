// app/src/main/java/com/example/tradeupsprojecy/data/models/request/CreateConversationRequest.java
package com.example.tradeupsprojecy.data.models.request;

public class CreateConversationRequest {
    private Long itemId;
    private String sellerId;
    private String buyerId;

    public CreateConversationRequest() {}

    // Getters and Setters
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
}