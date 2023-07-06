package com.kenzie.appserver.controller.reviewModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributeValuesForReviews {

    @JsonProperty("animeID")

    private int animeID;

    @JsonProperty("postDate")
    private int postDate;

    @JsonProperty("reviewID")
    private String reviewID;
    public int getAnimeID() {
        return animeID;
    }
    public void setAnimeID(int animeID) {
        this.animeID = animeID;
    }

    public int getPostDate() {
        return postDate;
    }

    public void setPostDate(int postDate) {
        this.postDate = postDate;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }
}
