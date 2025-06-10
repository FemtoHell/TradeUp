// app/src/main/java/com/example/tradeupsprojecy/data/models/request/CreateItemRequest.java
package com.example.tradeupsprojecy.data.models.request;

import java.math.BigDecimal;

public class CreateItemRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Long sellerId;
    private Long categoryId;
    private String condition;
    private String location;
    private String images;
    private String tags;
    private Boolean isNegotiable;

    public CreateItemRequest() {}

    // Constructor
    public CreateItemRequest(String title, String description, BigDecimal price, Long sellerId, Long categoryId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Boolean getIsNegotiable() { return isNegotiable; }
    public void setIsNegotiable(Boolean isNegotiable) { this.isNegotiable = isNegotiable; }
}