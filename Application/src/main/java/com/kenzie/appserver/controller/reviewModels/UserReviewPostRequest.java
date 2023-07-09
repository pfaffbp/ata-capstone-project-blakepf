package com.kenzie.appserver.controller.reviewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserReviewPostRequest {
    @NotEmpty
    @NotNull
    @JsonProperty("review")
    private String review;

    @NotEmpty
    @NotNull
    @JsonProperty("animeID")
    private int animeID;

    @NotEmpty
    @NotNull
    @JsonProperty("displayName")
    private String displayName;

    @Min(0)
    @Max(100)
    @JsonProperty("rating")
    private int rating;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getAnimeID() {
        return animeID;
    }

    public void setAnimeID(int animeID) {
        this.animeID = animeID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
