// app/src/main/java/com/example/tradeupsprojecy/data/models/request/UpdateUserRequest.java
package com.example.tradeupsprojecy.data.models.request;

public class UpdateUserRequest {
    private String displayName;
    private String bio;
    private String contactInfo;
    private String address;
    private String profilePicture;

    public UpdateUserRequest() {}

    // Getters and setters
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
}