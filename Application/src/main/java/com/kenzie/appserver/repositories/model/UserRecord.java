package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "UserTable")
public class UserRecord {
    private List<String> followers;
    private List<String> following;
    private String email;
    private String userId;
    private List<String> favoriteAnime;
    private String fullName;
    private String displayName;
    private int age;
    private String bio;



    @DynamoDBAttribute(attributeName = "email")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "email", attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "fullName")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    @DynamoDBAttribute(attributeName = "followers")
    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    @DynamoDBAttribute(attributeName = "following")
    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    @DynamoDBAttribute(attributeName = "favoriteAnime")
    public List<String> getFavoriteAnime() {
        return favoriteAnime;
    }

    public void setFavoriteAnime(List<String> favoriteAnime) {
        this.favoriteAnime = favoriteAnime;
    }

    @DynamoDBAttribute(attributeName = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @DynamoDBAttribute(attributeName = "bio")
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    @DynamoDBAttribute(attributeName = "displayName")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "displayName", attributeName = "displayName")
    public String getDisplayName() {return displayName;}

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRecord that = (UserRecord) o;
        return age == that.age && Objects.equals(followers, that.followers) && Objects.equals(email, that.email) && userId.equals(that.userId) && Objects.equals(favoriteAnime, that.favoriteAnime) && Objects.equals(fullName, that.fullName) && displayName.equals(that.displayName) && Objects.equals(bio, that.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followers, email, userId, favoriteAnime, fullName, displayName, age, bio);
    }
}