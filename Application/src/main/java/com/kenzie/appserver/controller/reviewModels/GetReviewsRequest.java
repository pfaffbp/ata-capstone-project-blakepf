package com.kenzie.appserver.controller.reviewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GetReviewsRequest {

    @JsonProperty("animeID")
    private int animeID;

    public int getAnimeID() {
        return animeID;
    }

    public void setAnimeID(int animeID) {
        this.animeID = animeID;
    }
}
