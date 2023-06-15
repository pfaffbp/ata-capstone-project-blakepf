package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CatalogCreateRequest;
import com.kenzie.appserver.controller.model.CatalogUpdateRequest;
import com.kenzie.appserver.service.CatalogService;
import com.kenzie.appserver.service.model.Anime;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class AnimeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    CatalogService catalogService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getAnime_AnimeExists() throws Exception {
        String title = mockNeat.strings().valStr();
        String animeId = UUID.randomUUID().toString();
        String description = mockNeat.strings().valStr();
        String image = mockNeat.strings().valStr();
        int startDate = 2008;
        String season = mockNeat.strings().valStr();
        int popularity = 100;
        int rating = 99;
        int episodes = 50;
        List<String> genre = new ArrayList<>();

        Anime anime = new Anime(title, animeId, description, image, startDate, season, popularity, rating, episodes, genre);
        Anime persistedAnime = catalogService.addNewAnime(anime);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(get("/anime/{animeId}", persistedAnime.getAnimeId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title")
                        .value(is(title)))
                .andExpect(jsonPath("animeId")
                        .value(is(animeId)))
                .andExpect(jsonPath("description")
                        .value(is(description)))
                .andExpect(jsonPath("image")
                        .value(is(image)))
                .andExpect(jsonPath("startDate")
                        .value(is(startDate)))
                .andExpect(jsonPath("season")
                        .value(is(season)))
                .andExpect(jsonPath("popularity")
                        .value(is(popularity)))
                .andExpect(jsonPath("rating")
                        .value(is(rating)))
                .andExpect(jsonPath("episodes")
                        .value(is(episodes)))
                .andExpect(jsonPath("genre")
                        .value(is(genre)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAnime_AnimeDoesNotExist() throws Exception {
        String animeId = UUID.randomUUID().toString();
        mvc.perform(get("/anime/{animeId}", animeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void createAnime_CreateSuccessful() throws Exception {
        String title = mockNeat.strings().valStr();
        String animeId = UUID.randomUUID().toString();
        String description = mockNeat.strings().valStr();
        String image = mockNeat.strings().valStr();
        int startDate = 2008;
        String season = mockNeat.strings().valStr();
        int popularity = 100;
        int rating = 99;
        int episodes = 50;
        List<String> genre = new ArrayList<>();

        CatalogCreateRequest animeCreateRequest = new CatalogCreateRequest();
        animeCreateRequest.setTitle(title);
        animeCreateRequest.setDescription(description);
        animeCreateRequest.setImage(image);
        animeCreateRequest.setStartDate(startDate);
        animeCreateRequest.setSeason(season);
        animeCreateRequest.setPopularity(popularity);
        animeCreateRequest.setRating(rating);
        animeCreateRequest.setEpisodes(episodes);
        animeCreateRequest.setGenre(genre);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/anime")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animeCreateRequest)))
                .andExpect(jsonPath("title")
                        .value(is(title)))
                .andExpect(jsonPath("animeId")
                        .exists())
                .andExpect(jsonPath("description")
                        .value(is(description)))
                .andExpect(jsonPath("image")
                        .value(is(image)))
                .andExpect(jsonPath("startDate")
                        .value(is(startDate)))
                .andExpect(jsonPath("season")
                        .value(is(season)))
                .andExpect(jsonPath("popularity")
                        .value(is(popularity)))
                .andExpect(jsonPath("rating")
                        .value(is(rating)))
                .andExpect(jsonPath("episodes")
                        .value(is(episodes)))
                .andExpect(jsonPath("genre")
                        .value(is(genre)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateAnime_PutSuccessful() throws Exception {
        String title = mockNeat.strings().valStr();
        String animeId = UUID.randomUUID().toString();
        String description = mockNeat.strings().valStr();
        String image = mockNeat.strings().valStr();
        int startDate = 2008;
        String season = mockNeat.strings().valStr();
        int popularity = 100;
        int rating = 99;
        int episodes = 50;
        List<String> genre = new ArrayList<>();

        Anime anime = new Anime(title, animeId, description, image, startDate, season, popularity, rating, episodes, genre);

        String newDescription = mockNeat.strings().valStr();
        int newRating = mockNeat.hashCode();

        CatalogUpdateRequest animeUpdateRequest = new CatalogUpdateRequest();
        animeUpdateRequest.setTitle(title);
        animeUpdateRequest.setAnimeId(animeId);
        animeUpdateRequest.setDescription(newDescription);
        animeUpdateRequest.setImage(image);
        animeUpdateRequest.setStartDate(startDate);
        animeUpdateRequest.setSeason(season);
        animeUpdateRequest.setPopularity(popularity);
        animeUpdateRequest.setRating(newRating);
        animeUpdateRequest.setEpisodes(episodes);
        animeUpdateRequest.setGenre(genre);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(put("/anime")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animeUpdateRequest)))
                .andExpect(jsonPath("title")
                        .value(is(title)))
                .andExpect(jsonPath("animeId")
                        .exists())
                .andExpect(jsonPath("description")
                        .value(is(newDescription)))
                .andExpect(jsonPath("image")
                        .value(is(image)))
                .andExpect(jsonPath("startDate")
                        .value(is(startDate)))
                .andExpect(jsonPath("season")
                        .value(is(season)))
                .andExpect(jsonPath("popularity")
                        .value(is(popularity)))
                .andExpect(jsonPath("rating")
                        .value(is(newRating)))
                .andExpect(jsonPath("episodes")
                        .value(is(episodes)))
                .andExpect(jsonPath("genre")
                        .value(is(genre)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteGame_DeleteSuccessful() throws Exception {
        String title = mockNeat.strings().valStr();
        String animeId = UUID.randomUUID().toString();
        String description = mockNeat.strings().valStr();
        String image = mockNeat.strings().valStr();
        int startDate = 2008;
        String season = mockNeat.strings().valStr();
        int popularity = 100;
        int rating = 99;
        int episodes = 50;
        List<String> genre = new ArrayList<>();

        Anime anime = new Anime(title, animeId, description, image, startDate, season, popularity, rating, episodes, genre);
        Anime persistedAnime = catalogService.addNewAnime(anime);

        mvc.perform(delete("/anime/{animeId}", persistedAnime.getAnimeId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertThat(catalogService.findAnimeById(animeId)).isNull();
    }
}
