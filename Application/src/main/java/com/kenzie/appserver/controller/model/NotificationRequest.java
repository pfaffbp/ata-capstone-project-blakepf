package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.capstone.service.model.UserRequest;

public class NotificationRequest {

    private String requestedUUID;

    private UserRequest userRequest;

    private boolean hasBeenViewed;

    public NotificationRequest() {
        // Default constructor
    }

    public NotificationRequest(String requestedUUID, UserRequest userRequest, boolean hasViewed) {
        this.requestedUUID = requestedUUID;
        this.userRequest = userRequest;
        this.hasBeenViewed = hasViewed;
    }

    @JsonProperty("requestUUID")
    public String getRequestedUUID() {
        return requestedUUID;
    }

    public void setRequestedUUID(String requestedUUID) {
        this.requestedUUID = requestedUUID;
    }
    @JsonProperty("userRequest")
    public UserRequest getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(UserRequest userRequest) {
        this.userRequest = userRequest;
    }

    @JsonProperty("hasBeenViewed")
    public boolean isHasBeenViewed() {
        return hasBeenViewed;
    }

    public void setHasBeenViewed(boolean hasViewed) {
        this.hasBeenViewed = hasViewed;
    }

}
