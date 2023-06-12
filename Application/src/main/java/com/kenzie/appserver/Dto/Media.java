package com.kenzie.appserver.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.Dto.CoverImage;
import com.kenzie.appserver.Dto.Title;

import java.util.List;

public class Media {
    @JsonProperty("title")
    private Title title;

    @JsonProperty("id")
    private int id;
    @JsonProperty("description")
    private String description;

    @JsonProperty("coverImage")
    private CoverImage coverImage;
    @JsonProperty("averageScore")
    private int averageScore;
    @JsonProperty("episodes")
    private int episodes;
    @JsonProperty("genres")
    private List<String> genres;

    @JsonProperty("startDate")
    private StartDate startDate;

    @JsonProperty("season")
    private String season;

    @JsonProperty("popularity")
    private int popularity;

    public StartDate getStartDate() {
        return startDate;
    }

    public void setStartDate(StartDate startDate) {
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

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CoverImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(CoverImage coverImage) {
        this.coverImage = coverImage;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}