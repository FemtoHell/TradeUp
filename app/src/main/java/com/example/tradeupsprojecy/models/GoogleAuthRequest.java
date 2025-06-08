package com.example.tradeupsprojecy.models;

public class GoogleAuthRequest {
    private String idToken;
    private String email;
    private String fullName;
    private String profileImageUrl;

    public GoogleAuthRequest() {}

    // Getters and setters
    public String getIdToken() { return idToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}