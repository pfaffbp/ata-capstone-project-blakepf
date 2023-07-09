package com.kenzie.capstone.service.model;

import java.util.Objects;

public class NotificationData {
    public String message;
    private String messageId;
    private String follower;
    private String following;

    public NotificationData(String message, String messageId, String follower, String following) {
        this.message = message;
        this.messageId = messageId;
        this.follower = follower;
        this.following = following;
    }

    public NotificationData() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationData that = (NotificationData) o;
        return Objects.equals(message, that.message) && Objects.equals(messageId, that.messageId) && Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, messageId, follower, following);
    }
}