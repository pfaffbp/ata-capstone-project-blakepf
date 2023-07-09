package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.Objects;

@DynamoDBDocument
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

    public UserRequest(String value) {
        String[] parts = value.split(":", 2);
        this.displayName = parts[0];
        this.action = parts[1];
    }

    @DynamoDBAttribute(attributeName = "displayName")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @DynamoDBAttribute(attributeName = "action")
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
        return this.getDisplayName() + ":" + this.getAction();
    }
}
