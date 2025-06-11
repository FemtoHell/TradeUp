// app/src/main/java/com/example/tradeupsprojecy/data/models/Listing.java
package com.example.tradeupsprojecy.data.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Listing {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String condition;
    private String location;
    private List<String> images;
    private List<String> imageUrls;
    private Long sellerId;
    private String sellerName;
    private String sellerPhone;
    private String sellerEmail;
    private Long categoryId;
    private String categoryName;
    private Date createdAt;
    private Date updatedAt;
    private boolean isSold;
    private boolean isFeatured;
    private int viewCount;
    private int likeCount;

    public Listing() {
        this.images = new ArrayList<>();
        this.imageUrls = new ArrayList<>();
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

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<String> getImages() {
        return images != null ? images : new ArrayList<>();
    }
    public void setImages(List<String> images) { this.images = images; }

    public List<String> getImageUrls() {
        return imageUrls != null ? imageUrls : new ArrayList<>();
    }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getSellerPhone() { return sellerPhone; }
    public void setSellerPhone(String sellerPhone) { this.sellerPhone = sellerPhone; }

    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public boolean isSold() { return isSold; }
    public void setSold(boolean sold) { isSold = sold; }

    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    // Helper method to get first image
    public String getFirstImage() {
        List<String> allImages = getImages();
        if (allImages != null && !allImages.isEmpty()) {
            return allImages.get(0);
        }

        List<String> allImageUrls = getImageUrls();
        if (allImageUrls != null && !allImageUrls.isEmpty()) {
            return allImageUrls.get(0);
        }

        return null;
    }
}