package com.kenzie.appserver.controller.reviewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewResponse {
    @JsonProperty("animeID")
    private int animeID;
    @JsonProperty("reviewID")
    private String reviewID;
    @JsonProperty("userID")
    private String userID;
    @JsonProperty("rating")
    private int rating;
    @JsonProperty("postDate")
    private int postDate;
    @JsonProperty("review")
    private String review;
    @JsonProperty("likes")
    private int likes;

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
