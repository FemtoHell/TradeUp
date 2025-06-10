// app/src/main/java/com/example/tradeupsprojecy/data/models/entities/Item.java
package com.example.tradeupsprojecy.data.models.entities;

import java.math.BigDecimal;

public class Item {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String condition;
    private String status;
    private String location;
    private String images;
    private String tags;
    private Boolean isNegotiable;
    private Integer viewCount;
    private String createdAt;
    private String updatedAt;

    // Seller information
    private User seller;
    private Long sellerId;

    // Category information
    private Category category;
    private Long categoryId;

    public Item() {}

    public Item(String title, String description, BigDecimal price, User seller, Category category) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.seller = seller;
        this.category = category;
        this.status = "AVAILABLE";
        this.viewCount = 0;
        this.isNegotiable = false;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Boolean getIsNegotiable() { return isNegotiable; }
    public void setIsNegotiable(Boolean isNegotiable) { this.isNegotiable = isNegotiable; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}