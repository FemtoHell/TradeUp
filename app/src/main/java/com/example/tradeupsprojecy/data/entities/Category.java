// app/src/main/java/com/example/tradeupsprojecy/data/models/entities/Category.java
package com.example.tradeupsprojecy.data.entities;

import java.util.List;

public class Category {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
    private String createdAt;
    private String updatedAt;

    // ✅ ADDED: Fields from other Category models
    private Boolean isActive = true;
    private List<Item> items; // Avoid circular dependency by using lazy loading

    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.isActive = true;
    }

    public Category(String name, String description, String iconUrl) {
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.isActive = true;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    // ✅ ADDED: Compatibility getters and setters
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    // ✅ ADDED: Alias methods for compatibility
    public List<Item> getListings() { return items; }
    public void setListings(List<Item> listings) { this.items = listings; }
}