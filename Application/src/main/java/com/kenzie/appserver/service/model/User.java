package com.kenzie.appserver.service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class User {
    private List<String> friends;
    //limit to 10
    private String email;
    private String userId;
    private List<String> favoriteAnime;
    private String fullName;
    private String displayName;
    private int age;
    private String bio;





    public User(String userId, String email, String fullName, int age, String displayName, String bio) {
        this.userId = userId;
        this.fullName = fullName;
        this.displayName = displayName;
        this.age = age;
        this.bio = bio;
        this.friends = new ArrayList<>();
        this.favoriteAnime = new ArrayList<>();
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFavoriteAnime() {
        return favoriteAnime;
    }

    public void setFavoriteAnime(List<String> favoriteAnime) {
        this.favoriteAnime = favoriteAnime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDisplayName() {return displayName;}

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
