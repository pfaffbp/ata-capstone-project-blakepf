package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "Reviews")
public class ReviewRecord {

    public static final String REVIEW_lOOK_UP = "ReviewLookUpGSI";

    public static final String AVERAGE_SCORE = "AverageScoreGsi";

    public static final String USER_LOOK_UP = "UserLookUpGsi";

    private int animeID;
    private String reviewID;
    private String displayName;
    private int rating;
    private int postDate;
    private String review;
    private int likes;

    @DynamoDBIndexHashKey(globalSecondaryIndexNames = {REVIEW_lOOK_UP, AVERAGE_SCORE}, attributeName = "animeID")
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

    @DynamoDBIndexHashKey(globalSecondaryIndexName = USER_LOOK_UP, attributeName = "displayName")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String userID) {
        this.displayName = userID;
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

    @DynamoDBAttribute(attributeName = "likes")
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
