package com.kenzie.appserver.controller;


import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.kenzie.appserver.DAO.Media;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.repositories.model.CatalogRecord;
import com.kenzie.appserver.service.CatalogService;
import com.kenzie.appserver.service.model.Anime;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        Anime anime = new Anime(catalogCreateRequest.getTitle(),
                randomUUID().toString(),
                catalogCreateRequest.getDescription(),
                catalogCreateRequest.getImage(),
                catalogCreateRequest.getStartDate(),
                catalogCreateRequest.getSeason(),
                catalogCreateRequest.getPopularity(),
                catalogCreateRequest.getRating(),
                catalogCreateRequest.getEpisodes(),
                catalogCreateRequest.getGenre());

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

        Anime anime = new Anime(catalogUpdateRequest.getTitle(),
                catalogUpdateRequest.getAnimeId(),
                catalogUpdateRequest.getDescription(),
                catalogUpdateRequest.getImage(),
                catalogUpdateRequest.getStartDate(),
                catalogUpdateRequest.getSeason(),
                catalogUpdateRequest.getPopularity(),
                catalogUpdateRequest.getRating(),
                catalogUpdateRequest.getEpisodes(),
                catalogUpdateRequest.getGenre());

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
        catalogResponse.setTitle(anime.getTitle());
        catalogResponse.setAnimeId(anime.getAnimeId());
        catalogResponse.setDescription(anime.getDescription());
        catalogResponse.setImage(anime.getImage());
        catalogResponse.setStartDate(anime.getStartDate());
        catalogResponse.setSeason(anime.getSeason());
        catalogResponse.setPopularity(anime.getPopularity());
        catalogResponse.setRating(anime.getRating());
        catalogResponse.setEpisodes(anime.getEpisodes());
        catalogResponse.setGenre(anime.getGenre());

        return catalogResponse;
    }

    private CatalogResponse catalogRecordToResponse(CatalogRecord anime){
        CatalogResponse catalogResponse = new CatalogResponse();
        catalogResponse.setTitle(anime.getTitle());
        catalogResponse.setAnimeId(anime.getAnimeId());
        catalogResponse.setDescription(anime.getDescription());
        catalogResponse.setImage(anime.getImage());
        catalogResponse.setStartDate(anime.getStartDate());
        catalogResponse.setSeason(anime.getSeason());
        catalogResponse.setPopularity(anime.getPopularity());
        catalogResponse.setRating(anime.getRating());
        catalogResponse.setEpisodes(anime.getEpisodes());
        catalogResponse.setGenre(anime.getGenre());

        return catalogResponse;
    }



    @GetMapping("/homePage")
    public ResponseEntity<List<CatalogResponse>> getFrontPageAnime(){
        List<PaginatedQueryList<CatalogRecord>> queryLists = new ArrayList<>();
        queryLists.add(catalogService.getSeasonAnime());

        List<CatalogResponse> scanList = new ArrayList<>();
        scanList.addAll(catalogService.getPopularAnime().stream()
                .map(this::catalogRecordToResponse)
                        .collect(Collectors.toList())
                );
        scanList.addAll(catalogService.getHighlyRated().stream()
                .map(this::catalogRecordToResponse)
                .collect(Collectors.toList())
        );

        queryLists.stream()
                .flatMap(Collection::stream)
                .map(this::catalogRecordToResponse)
                .forEach(scanList::add);

        return ResponseEntity.ok(scanList);
    }

    @PostMapping("postSearch")
    public void addSearchAnime(@RequestBody FrontPageAnimeRequest frontPageAnimeRequest){
        frontPageAnimeRequest.getGraphQLResponse()
                .getData()
                .getPage()
                .getMedia().stream()
                .map(CatalogController::responseToAnime)
                .forEach(anime -> catalogService.addNewAnime(anime));
    }

    private static Anime responseToAnime(Media catalogResponse){
        Anime anime = new Anime(catalogResponse.getTitle().getUserPreferred(),
                String.valueOf(catalogResponse.getId()),
                catalogResponse.getDescription(),
                catalogResponse.getCoverImage().getLarge(),
                catalogResponse.getStartDate().getYear(),
                catalogResponse.getSeason(),
                catalogResponse.getPopularity(),
                catalogResponse.getAverageScore(),
                catalogResponse.getEpisodes(),
                catalogResponse.getGenres());

        return anime;
    }
}

