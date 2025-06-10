// app/src/main/java/com/example/tradeupsprojecy/data/models/response/AuthResponse.java
package com.example.tradeupsprojecy.data.models.response;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private UserDto user;

    public AuthResponse() {}

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

    public static class UserDto {
        private Long id;
        private String fullName;
        private String email;
        private String profileImageUrl;
        private Boolean isVerified;
        private Double averageRating;
        private Integer totalRatings;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getProfileImageUrl() { return profileImageUrl; }
        public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

        public Boolean getIsVerified() { return isVerified; }
        public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

        public Double getAverageRating() { return averageRating; }
        public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

        public Integer getTotalRatings() { return totalRatings; }
        public void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }
    }
}