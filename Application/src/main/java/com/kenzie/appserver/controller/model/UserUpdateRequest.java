package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.User;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class UserUpdateRequest {
    @JsonProperty("followers")
    private List<String> followers;
    @JsonProperty("following")
    private List<String> following;
    @JsonProperty("favoriteAnime")
    private List<String> favoriteAnime;

    @JsonProperty("fullName")
    private String fullName;
    @NotEmpty
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("age")
    private int age;
    @JsonProperty("bio")
    private String bio;

    @JsonProperty("email")
    private String email;

    @NotEmpty
    @JsonProperty("userId")
    private String userId;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getDisplayName() { return displayName; }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}