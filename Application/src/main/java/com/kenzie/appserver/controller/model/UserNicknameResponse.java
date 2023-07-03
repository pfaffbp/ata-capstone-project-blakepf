package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserNicknameResponse {
    @JsonProperty("displayName")
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
