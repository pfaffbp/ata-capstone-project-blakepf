package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheAnimeStore;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.repositories.model.CatalogRecord;
import com.kenzie.appserver.service.model.Anime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CatalogService {

    private CatalogRepository catalogRepository;
    private CacheAnimeStore cache;

    public CatalogService(CatalogRepository catalogRepository, CacheAnimeStore cache) {
        this.catalogRepository = catalogRepository;
        this.cache = cache;
    }

    //Do we use CatalogResponse or Anime?
    public Anime findAnimeById(String animeId) {

        Anime cachedAnime = cache.get(animeId);

        if (cachedAnime != null) {
            return cachedAnime;
        }

        Anime animeStored = catalogRepository
                .findById(animeId)
                .map(anime -> new Anime(anime.getAnimeId(),
                        anime.getTitle(),
                        anime.getRating(),
                        anime.getYearReleased(),
                        anime.getGenre(),
                        anime.getEpisodes(),
                        anime.getDescription()))
                .orElse(null);

        if (animeStored != null) {
            cache.add(animeStored.getAnimeId(), animeStored);
        }

        return animeStored;
    }

    public List<Anime> findAllAnime() {
        List<Anime> animes = new ArrayList<>();

        Iterable<CatalogRecord> animeIterator = catalogRepository.findAll();

        for(CatalogRecord record : animeIterator) {
            animes.add(new Anime(record.getAnimeId(),
                    record.getTitle(),
                    record.getRating(),
                    record.getYearReleased(),
                    record.getGenre(),
                    record.getEpisodes(),
                    record.getDescription()));
        }

        return animes;
    }

    /* -----------------------------------------------------------------------------------------------------------
        Private Methods
       ----------------------------------------------------------------------------------------------------------- */


}
