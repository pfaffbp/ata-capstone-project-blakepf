package com.kenzie.appserver.model;

import java.util.Objects;


public class NotificationData {
    private String requestedUUID;

    private String userRequest;

    private boolean hasBeenViewed;

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


    public String getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(String userRequest) {
        this.userRequest = userRequest;
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
        NotificationData data = (NotificationData) o;
        return hasBeenViewed == data.hasBeenViewed && Objects.equals(requestedUUID, data.requestedUUID) && Objects.equals(userRequest, data.userRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestedUUID, userRequest, hasBeenViewed);
    }
}
