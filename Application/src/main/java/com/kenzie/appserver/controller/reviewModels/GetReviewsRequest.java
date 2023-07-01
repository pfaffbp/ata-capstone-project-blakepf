package com.kenzie.appserver.controller.reviewModels;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class GetReviewsRequest {

    @JsonProperty("animeID")
    private int animeID;

    @JsonProperty("valuesForReviews")
    private AttributeValuesForReviews valuesForReviews;

    public int getAnimeID() {
        return animeID;
    }

    public void setAnimeID(int animeID) {
        this.animeID = animeID;
    }


    public AttributeValuesForReviews getValuesForReviews() {
        return valuesForReviews;
    }

    public void setValuesForReviews(AttributeValuesForReviews valuesForReviews) {
        this.valuesForReviews = valuesForReviews;
    }
}
