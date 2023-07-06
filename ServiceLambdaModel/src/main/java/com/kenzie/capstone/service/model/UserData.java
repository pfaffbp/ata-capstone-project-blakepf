package com.kenzie.capstone.service.model;

import java.util.List;
import java.util.Objects;

public class UserData {

    private List<String> followers;
    private List<String> following;
    private String email;
    private String userId;
    private List<String> favoriteAnime;
    private String fullName;
    private String displayName;
    private int age;
    private String bio;

    public UserData(List<String> followers, List<String> following, String email, String userId, List<String> favoriteAnime, String fullName, String displayName, int age, String bio) {
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

    public UserData() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return age == userData.age && Objects.equals(followers, userData.followers) && Objects.equals(following, userData.following) && Objects.equals(email, userData.email) && Objects.equals(userId, userData.userId) && Objects.equals(favoriteAnime, userData.favoriteAnime) && Objects.equals(fullName, userData.fullName) && Objects.equals(displayName, userData.displayName) && Objects.equals(bio, userData.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followers, following, email, userId, favoriteAnime, fullName, displayName, age, bio);
    }
}
