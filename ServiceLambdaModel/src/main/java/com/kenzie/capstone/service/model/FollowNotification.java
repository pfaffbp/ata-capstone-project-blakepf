package com.kenzie.capstone.service.model;

import java.util.List;
import java.util.Objects;

public class FollowNotification {

    private String userDisplayName;
    private String friendDisplayName;

    public FollowNotification(String userDisplayName, String friendDisplayName) {
        this.userDisplayName = userDisplayName;
        this.friendDisplayName = friendDisplayName;
    }

    public FollowNotification () {}

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getFriendDisplayName() {
        return friendDisplayName;
    }

    public void setFriendDisplayName(String friendDisplayName) {
        this.friendDisplayName = friendDisplayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowNotification that = (FollowNotification) o;
        return Objects.equals(userDisplayName, that.userDisplayName) && Objects.equals(friendDisplayName, that.friendDisplayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDisplayName, friendDisplayName);
    }

}
