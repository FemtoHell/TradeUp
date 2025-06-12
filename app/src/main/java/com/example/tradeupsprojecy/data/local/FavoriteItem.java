package com.example.tradeupsprojecy.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_items")
public class FavoriteItem {
    @PrimaryKey
    @NonNull
    public Long itemId;
    public String title;
    public String description;
    public Double price;
    public String imageUrl;
    public String location;
    public long dateAdded;

    public FavoriteItem(Long itemId, String title, String description, Double price, String imageUrl, String location) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.location = location;
        this.dateAdded = System.currentTimeMillis();
    }

    // Getters and setters
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public long getDateAdded() { return dateAdded; }
    public void setDateAdded(long dateAdded) { this.dateAdded = dateAdded; }
}