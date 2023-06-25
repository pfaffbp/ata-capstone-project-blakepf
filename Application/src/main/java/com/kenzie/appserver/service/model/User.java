package com.kenzie.appserver.service.model;

public class User {

    private String userId;
    private String name;
    private String email;
    private String username;

    private String birthday;

    public User(String userId, String name, String email, String username, String birthday) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.username = username;
        this.birthday = birthday;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getBirthday() {
        return birthday;
    }
}
