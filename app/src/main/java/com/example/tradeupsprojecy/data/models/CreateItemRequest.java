// app/src/main/java/com/example/tradeupsprojecy/data/models/CreateItemRequest.java
package com.example.tradeupsprojecy.data.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateItemRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private String condition;
    private String location;
    private Long categoryId;
    private List<String> imageUrls;

    public CreateItemRequest() {
        this.imageUrls = new ArrayList<>();
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public List<String> getImageUrls() {
        return imageUrls != null ? imageUrls : new ArrayList<>();
    }
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
    }
}