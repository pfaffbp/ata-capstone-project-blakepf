package com.kenzie.appserver.service.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<String> followers;
    private List<String> following;
    private String email;
    private String userId;
    private List<String> favoriteAnime;
    private String fullName;
    private String displayName;
    private int age;
    private String bio;

    public User(List<String> followers, List<String> following, String email, String userId, List<String> favoriteAnime, String fullName, String displayName, int age, String bio) {
        this.followers = followers;
        this.following = following;
        this.email = email;
        this.userId = userId;
        this.favoriteAnime = favoriteAnime;
        this.fullName = fullName;
        this.displayName = displayName;
        this.age = age;
        this.bio = bio;
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

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
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