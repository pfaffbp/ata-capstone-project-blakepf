package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.User;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "UserTable")
public class UserRecord {
    private List<User> friends;
    private List<Anime> favoriteAnime;
    private String fullName;
    private int age;
    private String bio;

    @DynamoDBHashKey(attributeName = "FullName")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    @DynamoDBAttribute(attributeName = "Friends")
    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    @DynamoDBAttribute(attributeName = "FavoriteAnime")
    public List<Anime> getFavoriteAnime() {
        return favoriteAnime;
    }

    public void setFavoriteAnime(List<Anime> favoriteAnime) {
        this.favoriteAnime = favoriteAnime;
    }

    @DynamoDBAttribute(attributeName = "Age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @DynamoDBAttribute(attributeName = "Bio")
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRecord that = (UserRecord) o;
        return age == that.age && friends.equals(that.friends) && favoriteAnime.equals(that.favoriteAnime)
                && fullName.equals(that.fullName) && bio.equals(that.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friends, favoriteAnime, fullName, age, bio);
    }
}
