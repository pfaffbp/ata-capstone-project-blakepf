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
        @NotEmpty
        @JsonProperty("fullName")
        private String fullName;
        @JsonProperty("age")
        private int age;
        @JsonProperty("bio")
        private String bio;

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
}
