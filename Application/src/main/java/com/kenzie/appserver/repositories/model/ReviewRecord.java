package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "Reviews")
public class ReviewRecord {

    public static final String REVIEW_lOOK_UP = "ReviewLookUpGSI";
    private int animeID;

    private String reviewID;
    private String userID;
    private int rating;
    private int postDate;
    private String review;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = REVIEW_lOOK_UP, attributeName = "animeID")
    public int getAnimeID() {
        return animeID;
    }

    public void setAnimeID(int animeID) {
        this.animeID = animeID;
    }

    @Id
    @DynamoDBHashKey(attributeName = "reviewID")
    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }
    @DynamoDBAttribute(attributeName = "userID")
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @DynamoDBAttribute(attributeName = "rating")
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = REVIEW_lOOK_UP, attributeName = "postDate")
    public int getPostDate() {
        return postDate;
    }

    public void setPostDate(int postDate) {
        this.postDate = postDate;
    }
    @DynamoDBAttribute(attributeName = "review")
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
