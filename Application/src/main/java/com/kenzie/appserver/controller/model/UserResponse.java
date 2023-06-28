package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.User;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    @JsonProperty("friends")
    private List<User> friends;
    @JsonProperty("favoriteAnime")
    private List<Anime> favoriteAnime;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("age")
    private int age;
    @JsonProperty("bio")
    private String bio;
    @JsonProperty("displayName")
    private String displayName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {return displayName; }
}
