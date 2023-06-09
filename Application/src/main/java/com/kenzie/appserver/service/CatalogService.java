package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheAnimeStore;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.repositories.model.CatalogRecord;
import com.kenzie.appserver.service.model.Anime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Anime addNewAnime(Anime anime) {

        CatalogRecord catalogRecord = new CatalogRecord();

        catalogRecord.setAnimeId(anime.getAnimeId());
        catalogRecord.setTitle(anime.getTitle());
        catalogRecord.setRating(anime.getRating());
        catalogRecord.setYearReleased(anime.getYearReleased());
        catalogRecord.setGenre(anime.getGenre());
        catalogRecord.setEpisodes(anime.getEpisodes());
        catalogRecord.setDescription(anime.getDescription());
        catalogRepository.save(catalogRecord);

        return anime;
    }

    public void updateAnime(Anime anime) {

        if (catalogRepository.existsById(anime.getAnimeId())) {
            CatalogRecord catalogRecord = new CatalogRecord();

            catalogRecord.setAnimeId(anime.getAnimeId());
            catalogRecord.setTitle(anime.getTitle());
            catalogRecord.setRating(anime.getRating());
            catalogRecord.setYearReleased(anime.getYearReleased());
            catalogRecord.setGenre(anime.getGenre());
            catalogRecord.setEpisodes(anime.getEpisodes());
            catalogRecord.setDescription(anime.getDescription());
            catalogRepository.save(catalogRecord);

            cache.evict(anime.getAnimeId());
        }
    }

    public void deleteAnime(String animeId) {

        catalogRepository.deleteById(animeId);
        cache.evict(animeId);
    }
}
