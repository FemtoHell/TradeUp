package com.example.tradeupsprojecy.data.models;

import java.time.LocalDateTime;

public class Conversation {
    private String id;
    private String otherUserName;
    private String otherUserAvatar;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Integer unreadCount = 0;
    private String listingTitle;
    private String listingImage;

    // Constructors
    public Conversation() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOtherUserName() { return otherUserName; }
    public void setOtherUserName(String otherUserName) { this.otherUserName = otherUserName; }

    public String getOtherUserAvatar() { return otherUserAvatar; }
    public void setOtherUserAvatar(String otherUserAvatar) { this.otherUserAvatar = otherUserAvatar; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public LocalDateTime getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(LocalDateTime lastMessageTime) { this.lastMessageTime = lastMessageTime; }

    public Integer getUnreadCount() { return unreadCount; }
    public void setUnreadCount(Integer unreadCount) { this.unreadCount = unreadCount; }

    public String getListingTitle() { return listingTitle; }
    public void setListingTitle(String listingTitle) { this.listingTitle = listingTitle; }

    public String getListingImage() { return listingImage; }
    public void setListingImage(String listingImage) { this.listingImage = listingImage; }
}