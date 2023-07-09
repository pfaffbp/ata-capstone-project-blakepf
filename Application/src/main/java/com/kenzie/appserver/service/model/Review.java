package com.kenzie.appserver.service.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Review {
    private int animeID;
    private String reviewID;
    private String userID;
    private int rating;
    private int postDate;
    private String review;
    private int likes;

    public Review(int animeID, String userID, int rating, String review) {
        this.animeID = animeID;
        this.reviewID = UUID.randomUUID().toString();
        this.userID = userID;
        this.rating = rating;
        this.postDate = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        this.review = review;
        this.likes = 0;
    }

    public Review(){}

    public int getAnimeID() {
        return animeID;
    }

    public void setAnimeID(int animeID) {
        this.animeID = animeID;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getPostDate() {
        return postDate;
    }

    public void setPostDate(int postDate) {
        this.postDate = postDate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
