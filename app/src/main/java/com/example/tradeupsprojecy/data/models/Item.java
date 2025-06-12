// app/src/main/java/com/example/tradeupsprojecy/data/models/Item.java - REPLACE hoàn toàn
package com.example.tradeupsprojecy.data.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class Item {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;

    // Backend trả về category object
    private Category category;

    @SerializedName("categoryName")
    private String categoryName;

    private String condition;
    private String location;

    // Backend trả về string, cần parse thành List
    private String images; // Raw string from backend

    private String tags;
    private String status;

    @SerializedName("isNegotiable")
    private Boolean isNegotiable;

    private Double latitude;
    private Double longitude;

    @SerializedName("viewCount")
    private Integer viewCount;

    @SerializedName("chatCount")
    private Integer chatCount;

    @SerializedName("offerCount")
    private Integer offerCount;

    // Backend trả về seller object
    private User seller;

    @SerializedName("sellerName")
    private String sellerName;

    @SerializedName("sellerEmail")
    private String sellerEmail;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    public Item() {}

    // Helper method để convert images string thành List
    public List<String> getImageUrls() {
        List<String> imageList = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            try {
                // Remove brackets and quotes, split by comma
                String cleanImages = images.replace("[", "").replace("]", "").replace("\"", "");
                if (!cleanImages.trim().isEmpty()) {
                    String[] imageArray = cleanImages.split(",");
                    for (String img : imageArray) {
                        String trimmed = img.trim();
                        if (!trimmed.isEmpty()) {
                            imageList.add(trimmed);
                        }
                    }
                }
            } catch (Exception e) {
                // If parsing fails, try single image
                imageList.add(images);
            }
        }
        return imageList;
    }

    // Helper method để get first image
    public String getFirstImage() {
        List<String> urls = getImageUrls();
        return urls.isEmpty() ? null : urls.get(0);
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

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getCategoryName() {
        return categoryName != null ? categoryName :
                (category != null ? category.getName() : null);
    }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getIsNegotiable() { return isNegotiable; }
    public void setIsNegotiable(Boolean isNegotiable) { this.isNegotiable = isNegotiable; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getChatCount() { return chatCount; }
    public void setChatCount(Integer chatCount) { this.chatCount = chatCount; }

    public Integer getOfferCount() { return offerCount; }
    public void setOfferCount(Integer offerCount) { this.offerCount = offerCount; }

    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }

    public String getSellerName() {
        return sellerName != null ? sellerName :
                (seller != null ? seller.getFullName() : null);
    }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getSellerEmail() {
        return sellerEmail != null ? sellerEmail :
                (seller != null ? seller.getEmail() : null);
    }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    // Compatibility với Listing
    public Long getSellerId() {
        return seller != null ? seller.getId() : null;
    }

    public Long getCategoryId() {
        return category != null ? category.getId() : null;
    }
}