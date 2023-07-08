package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Map;
import java.util.Objects;

@DynamoDBTable(tableName = "NotificationTable")

public class NotificationRecord {

    private String requestedUUID;

    private UserRequest userRequest;

    private boolean hasBeenViewed;

    @DynamoDBHashKey(attributeName = "requestedUUID")
    public String getRequestedUUID() {
        return requestedUUID;
    }

    public void setRequestedUUID(String requestedUUID) {
        this.requestedUUID = requestedUUID;
    }


    @DynamoDBAttribute(attributeName = "userRequest")
    public UserRequest getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(UserRequest request) {
        this.userRequest = request;
    }

    @DynamoDBAttribute( attributeName = "hasBeenViewed")
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


