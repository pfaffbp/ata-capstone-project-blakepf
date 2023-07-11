package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.Map;
import java.util.Objects;

@DynamoDBTable(tableName = "NotificationTable")

public class NotificationRecord {

    public static final String NOTIFICATION_GET = "GrabNotification";
    private String requestedUUID;

    private String userRequest;

    private boolean hasBeenViewed;

    @DynamoDBHashKey(attributeName = "requestedUUID")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = NOTIFICATION_GET, attributeName = "requestUUID")
    public String getRequestedUUID() {
        return requestedUUID;
    }

    public void setRequestedUUID(String requestedUUID) {
        this.requestedUUID = requestedUUID;
    }


//    @DynamoDBRangeKey(attributeName = "userRequest")
//    public String getUserRequest() {
//         return userRequest.getDisplayName() + ":" + userRequest.getAction();
//    }
//
//    public void setUserRequest(UserRequest request) {
//        this.userRequest = request;
//    }

    @DynamoDBRangeKey(attributeName = "userRequest")
    public String getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(String userRequest) {
        this.userRequest = userRequest;
    }

    @DynamoDBAttribute(attributeName = "hasBeenViewed")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = NOTIFICATION_GET, attributeName = "hasBeenViewed")
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
        NotificationRecord that = (NotificationRecord) o;
        return hasBeenViewed == that.hasBeenViewed && Objects.equals(requestedUUID, that.requestedUUID) && Objects.equals(userRequest, that.userRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestedUUID, userRequest, hasBeenViewed);
    }

}

