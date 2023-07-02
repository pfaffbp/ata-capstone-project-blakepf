package com.kenzie.appserver.controller.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.User;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class UserCreateRequest {
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
        @NotEmpty
        @JsonProperty("displayName")
        private String displayName;

        @NotEmpty
        @JsonProperty("userId")
        private String userId;

        @JsonProperty("email")
        private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
