package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;

public class FavoriteAnimeRequest {
    @NotEmpty
    @JsonProperty
    private HashMap<Integer, String> favoriteAnime;

}
