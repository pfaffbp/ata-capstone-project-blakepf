package com.kenzie.capstone.service.model;

import java.util.Objects;

public class SetNotificationData {

    private String requestedUUID;

    private UserRequest userRequest;

    private boolean hasBeenViewed;

    public SetNotificationData(){}

    public SetNotificationData(String value){
        String[] strings = value.split(":", 2);
        userRequest.setDisplayName(strings[0]);
        userRequest.setAction(strings[1]);
    }
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


    public UserRequest getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(UserRequest userRequest) {
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
        SetNotificationData data = (SetNotificationData) o;
        return hasBeenViewed == data.isHasBeenViewed() && Objects.equals(requestedUUID, data.getRequestedUUID()) && Objects.equals(userRequest, data.getUserRequest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestedUUID, userRequest, hasBeenViewed);
    }
}