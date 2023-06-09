package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.User;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "UserTable")
public class UserRecord {
    private String username;
    private List<User> friends;
    private List<Anime> favoriteAnime;
    private String email;
    private String password;
    private String fullName;
    private int age;
    private String bio;

    @DynamoDBHashKey(attributeName = "Username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @DynamoDBAttribute(attributeName = "Email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBRangeKey(attributeName = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBAttribute(attributeName = "FullName")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        return getAge() == that.getAge() && getUsername().equals(that.getUsername()) && Objects.equals(getFriends(), that.getFriends())
                && getFavoriteAnime().equals(that.getFavoriteAnime()) && getEmail().equals(that.getEmail()) && getPassword().equals(that.getPassword())
                && getFullName().equals(that.getFullName()) && Objects.equals(getBio(), that.getBio());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getFriends(), getFavoriteAnime(), getEmail(), getPassword(), getFullName(), getAge(), getBio());
    }
}
