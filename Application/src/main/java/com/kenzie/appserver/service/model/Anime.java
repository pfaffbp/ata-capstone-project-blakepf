package com.kenzie.appserver.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class Anime {

    private String title;
    private String animeId;
    private String description;
    private String image;
    private int startDate;
    private String season;
    private int popularity;
    private int rating;
    private int episodes;
    private List<String> genre;

    public Anime(String title, String animeId, String description, String image,
                 int startDate, String season, int popularity, int rating, int episodes, List<String> genre) {
        this.title = title;
        this.animeId = animeId;
        this.description = description;
        this.image = image;
        this.startDate = startDate;
        this.season = season;
        this.popularity = popularity;
        this.rating = rating;
        this.episodes = episodes;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnimeId() {
        return animeId;
    }

    public void setAnimeId(String animeId) {
        this.animeId = animeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }
}
