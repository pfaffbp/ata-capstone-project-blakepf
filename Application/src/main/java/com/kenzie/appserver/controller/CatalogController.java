package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CatalogCreateRequest;
import com.kenzie.appserver.controller.model.CatalogResponse;
import com.kenzie.appserver.controller.model.CatalogUpdateRequest;
import com.kenzie.appserver.service.CatalogService;
import com.kenzie.appserver.service.model.Anime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/anime")
public class CatalogController {

    private CatalogService catalogService;

    CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/{animeId}")
    public ResponseEntity<CatalogResponse> searchAnimeById(@PathVariable("animeId") String animeId) {

        Anime anime = catalogService.findAnimeById(animeId);

        if (anime == null) {
            return ResponseEntity.notFound().build();
        }

        CatalogResponse catalogResponse = createCatalogResponse(anime);

        return ResponseEntity.ok(catalogResponse);
    }

    @PostMapping
    public ResponseEntity<CatalogResponse> addNewAnime(@RequestBody CatalogCreateRequest catalogCreateRequest) {
        Anime anime = new Anime(randomUUID().toString(),
                catalogCreateRequest.getTitle(),
                catalogCreateRequest.getRating(),
                catalogCreateRequest.getYearReleased(),
                catalogCreateRequest.getGenre(),
                catalogCreateRequest.getEpisodes(),
                catalogCreateRequest.getDescription());

        catalogService.addNewAnime(anime);

        CatalogResponse catalogResponse = createCatalogResponse(anime);

        return ResponseEntity.created(URI.create("/anime/" + catalogResponse.getAnimeId())).body(catalogResponse);
    }

    @GetMapping
    public ResponseEntity<List<CatalogResponse>> getAllAnime() {

        List<Anime> anime = catalogService.findAllAnime();

        if (anime == null ||  anime.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<CatalogResponse> response = new ArrayList<>();

        for (Anime animes : anime) {
            response.add(this.createCatalogResponse(animes));
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CatalogResponse> updateAnime(@RequestBody CatalogUpdateRequest catalogUpdateRequest) {

        Anime anime = new Anime(catalogUpdateRequest.getAnimeId(),
                catalogUpdateRequest.getTitle(),
                catalogUpdateRequest.getRating(),
                catalogUpdateRequest.getYearReleased(),
                catalogUpdateRequest.getGenre(),
                catalogUpdateRequest.getEpisodes(),
                catalogUpdateRequest.getDescription());

        catalogService.updateAnime(anime);

        CatalogResponse concertResponse = createCatalogResponse(anime);

        return ResponseEntity.ok(concertResponse);
    }

    @DeleteMapping("/{animeId}")
    public ResponseEntity deleteAnimeById(@PathVariable("animeId") String animeId) {

        catalogService.deleteAnime(animeId);
        return ResponseEntity.status(204).build();
    }

    private CatalogResponse createCatalogResponse(Anime anime) {

        CatalogResponse catalogResponse = new CatalogResponse();
        catalogResponse.setAnimeId(anime.getAnimeId());
        catalogResponse.setTitle(anime.getTitle());
        catalogResponse.setRating(anime.getRating());
        catalogResponse.setYearReleased(anime.getYearReleased());
        catalogResponse.setGenre(anime.getGenre());
        catalogResponse.setEpisodes(anime.getEpisodes());
        catalogResponse.setDescription(anime.getDescription());

        return catalogResponse;
    }
}

