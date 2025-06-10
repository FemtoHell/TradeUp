// app/src/main/java/com/example/tradeupsprojecy/data/models/GoogleAuthRequest.java
package com.example.tradeupsprojecy.data.models;

public class GoogleAuthRequest {
    private String idToken;
    private String email;
    private String fullName;
    private String profileImageUrl;

    public GoogleAuthRequest() {}

    public GoogleAuthRequest(String idToken, String email, String fullName, String profileImageUrl) {
        this.idToken = idToken;
        this.email = email;
        this.fullName = fullName;
        this.profileImageUrl = profileImageUrl;
    }

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