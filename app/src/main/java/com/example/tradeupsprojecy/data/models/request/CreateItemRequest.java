// app/src/main/java/com/example/tradeupsprojecy/data/models/request/CreateItemRequest.java
package com.example.tradeupsprojecy.data.models.request;

import java.math.BigDecimal;
import java.util.List;

public class CreateItemRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private String location;
    private String condition;
    private List<String> imageUrls;

    public CreateItemRequest() {}

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}