package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserIdResponse {
    @JsonProperty("userId")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
