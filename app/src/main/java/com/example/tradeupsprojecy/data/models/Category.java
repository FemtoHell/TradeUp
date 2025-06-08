package com.example.tradeupsprojecy.data.models;

import java.util.List;

public class Category {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
    private Boolean isActive = true;
    private List<Listing> listings;

    // Constructors
    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<Listing> getListings() { return listings; }
    public void setListings(List<Listing> listings) { this.listings = listings; }
}