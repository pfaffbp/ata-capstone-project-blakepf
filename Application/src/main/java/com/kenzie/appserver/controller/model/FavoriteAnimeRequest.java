package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Anime;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;

public class FavoriteAnimeRequest {
    @NotEmpty
    @JsonProperty
    private HashMap<Integer, String> favoriteAnime;

    public HashMap<Integer, String> getFavoriteAnime() {
        return favoriteAnime;
    }
    public void setFavoriteAnime(HashMap<Integer, String> favoriteAnime) {
        this.favoriteAnime = favoriteAnime;
    }
}
