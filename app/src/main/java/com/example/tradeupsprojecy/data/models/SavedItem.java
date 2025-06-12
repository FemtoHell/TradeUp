// app/src/main/java/com/example/tradeupsprojecy/data/models/SavedItem.java
package com.example.tradeupsprojecy.data.models;

import java.time.LocalDateTime;

public class SavedItem {
    private Long id;
    private User user;
    private Listing item;
    private LocalDateTime createdAt;

    // Constructors
    public SavedItem() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Listing getItem() { return item; }
    public void setItem(Listing item) { this.item = item; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}