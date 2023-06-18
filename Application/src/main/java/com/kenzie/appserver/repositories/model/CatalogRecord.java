package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "AnimeCatalog")
public class CatalogRecord {
    public static final String SEASONAL_ANIME_INDEX = "SeasonAnime";

    public static final String POPULAR_ANIME_INDEX = "PopularAnime";

    public static final String HIGHLY_RATED_INDEX = "HighlyRated";

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


    @DynamoDBIndexHashKey(globalSecondaryIndexName = HIGHLY_RATED_INDEX, attributeName = "Title")
    @DynamoDBAttribute(attributeName = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBHashKey(attributeName = "AnimeId")
    public String getAnimeId() {
        return animeId;
    }

    public void setAnimeId(String animeId) {
        this.animeId = animeId;
    }

    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "Image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = SEASONAL_ANIME_INDEX, attributeName = "StartDate")
    @DynamoDBAttribute(attributeName = "StartDate")
    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = SEASONAL_ANIME_INDEX, attributeName = "Season")
    @DynamoDBAttribute(attributeName = "Season")
    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = POPULAR_ANIME_INDEX, attributeName = "Popularity")
    @DynamoDBAttribute(attributeName = "Popularity")
    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }


    @DynamoDBIndexRangeKey(globalSecondaryIndexName = HIGHLY_RATED_INDEX, attributeName = "Rating")
    @DynamoDBAttribute(attributeName = "Rating")
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @DynamoDBAttribute(attributeName = "Episodes")
    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    @DynamoDBAttribute(attributeName = "Genre")
    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatalogRecord that = (CatalogRecord) o;
        return popularity == that.popularity && rating == that.rating && episodes == that.episodes && title.equals
                (that.title) && animeId.equals(that.animeId) && description.equals(that.description) && image.equals
                (that.image) && startDate == (that.startDate) && season.equals(that.season) && genre.equals
                (that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, animeId, description, image, startDate, season, popularity, rating, episodes, genre);
    }
}
