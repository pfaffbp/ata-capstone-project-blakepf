package com.kenzie.appserver.service.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private List<User> friends;
    private List<Anime> favoriteAnime;
    private String fullName;
    private String displayName;
    private int age;
    private String bio;



    public User(String fullName, int age, String displayName, String bio) {
        this.fullName = fullName;
        this.displayName = displayName;
        this.age = age;
        this.bio = bio;
        this.friends = new ArrayList<>();
        this.favoriteAnime = new ArrayList<>();
    }
    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<Anime> getFavoriteAnime() {
        return favoriteAnime;
    }

    public void setFavoriteAnime(List<Anime> favoriteAnime) {
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
