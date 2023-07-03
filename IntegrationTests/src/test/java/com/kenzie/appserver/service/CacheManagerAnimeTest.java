package com.kenzie.appserver.service;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.config.CacheAnimeStore;
import com.kenzie.appserver.service.model.Anime;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class CacheManagerAnimeTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    CatalogService catalogService;

    @Autowired
    private CacheAnimeStore Cache;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @Test
    public void animeCache_InsertIntoCache() throws Exception {

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

        Anime anime = new Anime("Title", animeId, "description",
                "image", 2008, "season",100, 90, 50, new ArrayList<>());
        catalogService.addNewAnime(anime);
        catalogService.findAnimeById(animeId);

        Anime animeFromCache = Cache.get(anime.getAnimeId());

        assertThat(animeFromCache).isNotNull();
        assertThat(animeFromCache.getAnimeId()).isEqualTo(animeId);
    }

    @Test
    public void animeCacheUpdate_EvictFromCache() throws Exception {
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

        Anime anime = new Anime("Title", animeId, "description",
                "image", 2008, "season",100, 90, 50, new ArrayList<>());
        catalogService.addNewAnime(anime);
        catalogService.findAnimeById(animeId);

        Anime animeFromCache = Cache.get(anime.getAnimeId());

        catalogService.updateAnime(anime);

        Anime updatedAnimeFromCache = Cache.get(anime.getAnimeId());

        assertThat(animeFromCache).isNotNull();
        assertThat(animeFromCache.getAnimeId()).isEqualTo(animeId);
        assertThat(updatedAnimeFromCache).isNull();
    }
}

