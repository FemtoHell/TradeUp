// app/src/main/java/com/example/tradeupsprojecy/data/models/entities/User.java
package com.example.tradeupsprojecy.data.entities;

public class User {
    private Long id;
    private String email;
    private String password;
    private String displayName;
    private String bio;
    private String contactInfo;
    private String address;
    private Double latitude;
    private Double longitude;
    private String profilePicture;
    private Double averageRating;
    private Integer totalTransactions;
    private Boolean emailVerified;
    private String accountStatus;
    private String createdAt;
    private String updatedAt;

    // ✅ ADDED: Thêm fields từ User models khác để tương thích
    private String fullName;
    private String profileImageUrl;
    private String phone;
    private Boolean isVerified;
    private Integer totalRatings;

    public User() {}

    public User(String email, String password, String displayName) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.averageRating = 0.0;
        this.totalTransactions = 0;
        this.emailVerified = false;
        this.accountStatus = "ACTIVE";
    }

    // ✅ ADDED: Constructor tương thích với User models khác
    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
        this.displayName = fullName; // Map fullName to displayName
        this.isVerified = false;
        this.averageRating = 0.0;
        this.totalRatings = 0;
        this.totalTransactions = 0;
        this.emailVerified = false;
        this.accountStatus = "ACTIVE";
    }

    // Original getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.fullName = displayName; // Keep in sync
    }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        this.profileImageUrl = profilePicture; // Keep in sync
    }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Integer getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(Integer totalTransactions) { this.totalTransactions = totalTransactions; }

    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        this.isVerified = emailVerified; // Keep in sync
    }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    // ✅ ADDED: Compatibility getters and setters
    public String getFullName() {
        return fullName != null ? fullName : displayName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
        this.displayName = fullName; // Keep in sync
    }

    public String getProfileImageUrl() {
        return profileImageUrl != null ? profileImageUrl : profilePicture;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        this.profilePicture = profileImageUrl; // Keep in sync
    }

    public String getPhone() {
        return phone != null ? phone : contactInfo;
    }
    public void setPhone(String phone) {
        this.phone = phone;
        this.contactInfo = phone; // Keep in sync
    }

    public Boolean getIsVerified() {
        return isVerified != null ? isVerified : emailVerified;
    }
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
        this.emailVerified = isVerified; // Keep in sync
    }

    public Integer getTotalRatings() {
        return totalRatings != null ? totalRatings : totalTransactions;
    }
    public void setTotalRatings(Integer totalRatings) {
        this.totalRatings = totalRatings;
    }
}