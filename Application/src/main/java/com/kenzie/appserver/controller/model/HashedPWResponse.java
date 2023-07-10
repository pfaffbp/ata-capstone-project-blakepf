package com.kenzie.appserver.controller.model;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HashedPWResponse {
    @JsonProperty("password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}