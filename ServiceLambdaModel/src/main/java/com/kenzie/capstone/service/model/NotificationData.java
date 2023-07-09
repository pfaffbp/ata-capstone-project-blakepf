package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
import java.util.Objects;



public class NotificationData {
    String requestedUUID;

    UserRequest userRequest;

    boolean hasBeenViewed;

    public NotificationData() {
        // Default constructor
    }

    public NotificationData(String requestedUUID, UserRequest userRequest, boolean hasViewed) {
        this.requestedUUID = requestedUUID;
        this.userRequest = userRequest;
        this.hasBeenViewed = hasViewed;
    }


    public String getRequestedUUID() {
        return requestedUUID;
    }

    public void setRequestedUUID(String requestedUUID) {
        this.requestedUUID = requestedUUID;
    }


    public UserRequest getRequest() {
        return userRequest;
    }

    public void setRequest(UserRequest request) {
        this.userRequest = request;
    }

    public boolean isHasBeenViewed() {
        return hasBeenViewed;
    }

    public void setHasBeenViewed(boolean hasBeenViewed) {
        this.hasBeenViewed = hasBeenViewed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationData that = (NotificationData) o;
        return hasBeenViewed == that.hasBeenViewed && requestedUUID.equals(that.requestedUUID) && userRequest.equals(that.userRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestedUUID, userRequest, hasBeenViewed);
    }


}
