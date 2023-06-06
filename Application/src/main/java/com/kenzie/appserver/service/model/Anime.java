package com.kenzie.appserver.service.model;

public class Anime {

    private String animeId;
    private String title;
    private double rating;
    private int yearReleased;
    private int genre;
    private int episodes;
    private String description;

    public Anime(String animeId, String title, double rating, int yearReleased, int genre, int episodes, String description) {
        this.animeId = animeId;
        this.title = title;
        this.rating = rating;
        this.yearReleased = yearReleased;
        this.genre = genre;
        this.episodes = episodes;
        this.description = description;
    }

    public String getAnimeId() {
        return animeId;
    }

    public void setAnimeId(String animeId) {
        this.animeId = animeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getYearReleased() {
        return yearReleased;
    }

    public void setYearReleased(int yearReleased) {
        this.yearReleased = yearReleased;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
