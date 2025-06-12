// app/src/main/java/com/example/tradeupsprojecy/data/models/Review.java
package com.example.tradeupsprojecy.data.models;

import java.time.LocalDateTime;

public class Review {
    private Long id;
    private User reviewer;
    private User reviewee;
    private Integer rating;
    private String comment;
    private Listing item;
    private LocalDateTime createdAt;

    // Constructors
    public Review() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getReviewer() { return reviewer; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }

    public User getReviewee() { return reviewee; }
    public void setReviewee(User reviewee) { this.reviewee = reviewee; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Listing getItem() { return item; }
    public void setItem(Listing item) { this.item = item; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}