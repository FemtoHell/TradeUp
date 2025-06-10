// app/src/main/java/com/example/tradeupsprojecy/data/models/Listing.java
package com.example.tradeupsprojecy.data.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
// ✅ FIXED: Chỉ import entities User
import com.example.tradeupsprojecy.data.models.User;
import com.example.tradeupsprojecy.data.entities.Category;

public class Listing {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean isNegotiable = false;
    private String condition;
    private String status = "ACTIVE";
    private String location;
    private Double latitude;
    private Double longitude;
    private List<String> imageUrls;
    private List<String> tags;
    private Integer viewCount = 0;
    private Boolean isFeatured = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User user;
    private Category category;

    // Constructors
    public Listing() {}

    public Listing(String title, String description, BigDecimal price, User user, Category category) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.user = user;
        this.category = category;
        this.status = "ACTIVE";
        this.viewCount = 0;
        this.isNegotiable = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Boolean getIsNegotiable() { return isNegotiable; }
    public void setIsNegotiable(Boolean isNegotiable) { this.isNegotiable = isNegotiable; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}