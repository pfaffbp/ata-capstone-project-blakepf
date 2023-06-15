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
                .map(anime -> new Anime(anime.getTitle(),
                        anime.getAnimeId(),
                        anime.getDescription(),
                        anime.getImage(),
                        anime.getStartDate(),
                        anime.getSeason(),
                        anime.getPopularity(),
                        anime.getRating(),
                        anime.getEpisodes(),
                        anime.getGenre()))
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
            animes.add(new Anime(record.getTitle(),
                    record.getAnimeId(),
                    record.getDescription(),
                    record.getImage(),
                    record.getStartDate(),
                    record.getSeason(),
                    record.getPopularity(),
                    record.getRating(),
                    record.getEpisodes(),
                    record.getGenre()));
        }

        return animes;
    }

    public Anime addNewAnime(Anime anime) {

        CatalogRecord catalogRecord = new CatalogRecord();

        catalogRecord.setTitle(anime.getTitle());
        catalogRecord.setAnimeId(anime.getAnimeId());
        catalogRecord.setDescription(anime.getDescription());
        catalogRecord.setImage(anime.getImage());
        catalogRecord.setStartDate(anime.getStartDate());
        catalogRecord.setSeason(anime.getSeason());
        catalogRecord.setPopularity(anime.getPopularity());
        catalogRecord.setRating(anime.getRating());
        catalogRecord.setEpisodes(anime.getEpisodes());
        catalogRecord.setGenre(anime.getGenre());
        catalogRepository.save(catalogRecord);

        return anime;
    }

    public void updateAnime(Anime anime) {

        if (catalogRepository.existsById(anime.getAnimeId())) {
            CatalogRecord catalogRecord = new CatalogRecord();

            catalogRecord.setTitle(anime.getTitle());
            catalogRecord.setAnimeId(anime.getAnimeId());
            catalogRecord.setDescription(anime.getDescription());
            catalogRecord.setImage(anime.getImage());
            catalogRecord.setStartDate(anime.getStartDate());
            catalogRecord.setSeason(anime.getSeason());
            catalogRecord.setPopularity(anime.getPopularity());
            catalogRecord.setRating(anime.getRating());
            catalogRecord.setEpisodes(anime.getEpisodes());
            catalogRecord.setGenre(anime.getGenre());
            catalogRepository.save(catalogRecord);

            cache.evict(anime.getAnimeId());
        }
    }

    public void deleteAnime(String animeId) {

        catalogRepository.deleteById(animeId);
        cache.evict(animeId);
    }
}
