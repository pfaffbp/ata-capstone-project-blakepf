package com.kenzie.appserver.service.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private List<User> friends;
    private List<Anime> favoriteAnime;
    private String email;
    private String password;
    private String fullName;
    private int age;
    private String bio;

    public User(String username) {
        this.username = username;
        this.friends = new ArrayList<>();
        this.favoriteAnime = new ArrayList<>();
    }

    public User(String username, String email, String password, String fullName, int age, String bio) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
        this.bio = bio;
        this.friends = new ArrayList<>();
        this.favoriteAnime = new ArrayList<>();
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
