package com.example.tradeupsprojecy.data.models;

public class User {
    private Long id;
    private String email;
    private String fullName;
    private String profileImageUrl;
    private String phone;
    private String bio;
    private Boolean isVerified;
    private Double averageRating;
    private Integer totalRatings;

    // Default constructor
    public User() {}

    // Constructor with basic info
    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
        this.isVerified = false;
        this.averageRating = 0.0;
        this.totalRatings = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Integer getTotalRatings() { return totalRatings; }
    public void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }
}