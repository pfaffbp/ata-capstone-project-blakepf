package com.kenzie.capstone.service.model;

import java.util.Objects;

public class UserRequest {
    String displayName;

    String action;

    public UserRequest() {
        // Default constructor
    }

    public UserRequest(String displayName, String action) {
        this.displayName = displayName;
        this.action = action;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRequest that = (UserRequest) o;
        return displayName.equals(that.displayName) && action.equals(that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, action);
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "displayName='" + displayName + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
