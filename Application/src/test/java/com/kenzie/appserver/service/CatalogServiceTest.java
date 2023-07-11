package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kenzie.appserver.config.CacheAnimeStore;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.repositories.model.CatalogRecord;
import com.kenzie.appserver.service.model.Anime;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class CatalogServiceTest {

    private CatalogRepository catalogRepository;
    private CacheAnimeStore cacheStore;
    private CatalogService catalogService;
    private final MockNeat mockNeat = MockNeat.threadLocal();
    private DynamoDBMapper mapper;


    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        mapper = mock(DynamoDBMapper.class);
        catalogRepository = mock(CatalogRepository.class);
        cacheStore = mock(CacheAnimeStore.class);
        catalogService = new CatalogService(catalogRepository, cacheStore, mapper);

    }

    @Test
    void deleteAnime() {
        String animeId = "id";
        List<String> test = new LinkedList<>();
        test.add("TEST");
        test.add("TEST2");

        CatalogRecord record = new CatalogRecord();
        record.setAnimeId(animeId);

        Mockito.when(catalogRepository.existsById(anyString())).thenReturn(true);
        Mockito.when(catalogRepository.findById(anyString())).thenReturn(Optional.of(record));

        when(cacheStore.get(record.getAnimeId())).thenReturn(new Anime("title", animeId, "description",
                "image", 2007, "season",300, 90, 500, test));

        catalogService.deleteAnime(animeId);

        verify(cacheStore).evict(record.getAnimeId());

        verify(catalogRepository, times(1)).deleteById(animeId);
    }

    @Test
    void catalogService_findByAnimeId_idIsNull() {
        CatalogRecord nullId = new CatalogRecord();
        nullId.setAnimeId(null);

        when(catalogRepository.findById(nullId.getAnimeId())).thenReturn(Optional.empty());
        Anime response = catalogService.findAnimeById(nullId.getAnimeId());

        Assertions.assertNull(response);
    }

    @Test
    void findByAnimeId() {
        String animeId = randomUUID().toString();

        CatalogRecord record = new CatalogRecord();
        record.setTitle("Anime Title");
        record.setAnimeId(animeId);
        record.setDescription("Description");
        record.setImage("Image");
        record.setStartDate(2007);
        record.setSeason("Season");
        record.setPopularity(300);
        record.setRating(90);
        record.setEpisodes(500);
        record.setGenre(new ArrayList<>());

        when(catalogRepository.findById(animeId)).thenReturn(Optional.of(record));

        Anime anime = catalogService.findAnimeById(animeId);

        Assertions.assertNotNull(anime, "The anime is returned");
        assertEquals(record.getTitle(), anime.getTitle(), "The anime title matches");
        assertEquals(record.getAnimeId(), anime.getAnimeId(), "The anime id matches");
        assertEquals(record.getDescription(), anime.getDescription(), "The anime description matches");
        assertEquals(record.getImage(), anime.getImage(), "The anime image matches");
        assertEquals(record.getStartDate(), anime.getStartDate(), "The anime start date matches");
        assertEquals(record.getSeason(), anime.getSeason(), "The anime season matches");
        assertEquals(record.getPopularity(), anime.getPopularity(), "The anime popularity matches");
        assertEquals(record.getRating(), anime.getRating(), "The anime rating matches");
        assertEquals(record.getEpisodes(), anime.getEpisodes(), "The anime episodes match");
        assertEquals(record.getGenre(), anime.getGenre(), "The anime genre matches");

    }

    @Test
    void addNewAnime() {
        String animeId = randomUUID().toString();

        Anime anime = new Anime("Title", animeId, "description",
                "image", 2007, "season",300, 90, 500, new ArrayList<>());

        ArgumentCaptor<CatalogRecord> animeRecordCaptor = ArgumentCaptor.forClass(CatalogRecord.class);

        Anime returnedAnime = catalogService.addNewAnime(anime);

        Assertions.assertNotNull(returnedAnime);

        verify(catalogRepository).save(animeRecordCaptor.capture());

        CatalogRecord record = animeRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The anime record is returned");
        assertEquals(record.getTitle(), anime.getTitle(), "The anime title matches");
        assertEquals(record.getAnimeId(), anime.getAnimeId(), "The anime id matches");
        assertEquals(record.getDescription(), anime.getDescription(), "The anime description matches");
        assertEquals(record.getImage(), anime.getImage(), "The anime image matches");
        assertEquals(record.getStartDate(), anime.getStartDate(), "The anime start date matches");
        assertEquals(record.getSeason(), anime.getSeason(), "The anime season matches");
        assertEquals(record.getPopularity(), anime.getPopularity(), "The anime popularity matches");
        assertEquals(record.getRating(), anime.getRating(), "The anime rating matches");
        assertEquals(record.getEpisodes(), anime.getEpisodes(), "The anime episodes match");
        assertEquals(record.getGenre(), anime.getGenre(), "The anime genre matches");

    }

    @Test
    public void updateAnime_validAnime_animeUpdated() {

        String animeId = randomUUID().toString();
        Anime anime = new Anime("Title", animeId, "description",
                "image", 2007, "season",300, 90, 500, new ArrayList<>());


        CatalogRecord animeCatalogRecord = new CatalogRecord();
        animeCatalogRecord.setTitle(anime.getTitle());
        animeCatalogRecord.setAnimeId(anime.getAnimeId());
        animeCatalogRecord.setDescription(anime.getDescription());
        animeCatalogRecord.setImage(anime.getImage());
        animeCatalogRecord.setStartDate(anime.getStartDate());
        animeCatalogRecord.setSeason(anime.getSeason());
        animeCatalogRecord.setPopularity(anime.getPopularity());
        animeCatalogRecord.setRating(anime.getRating());
        animeCatalogRecord.setEpisodes(anime.getEpisodes());
        animeCatalogRecord.setGenre(anime.getGenre());

        when(catalogRepository.existsById(anime.getAnimeId())).thenReturn(true);

        ArgumentCaptor<CatalogRecord> argumentCaptor = ArgumentCaptor.forClass(CatalogRecord.class);
        when(catalogRepository.findById(anime.getAnimeId())).thenReturn(Optional.of(animeCatalogRecord));
        when(catalogRepository.save(argumentCaptor.capture())).thenReturn(animeCatalogRecord);
        when(cacheStore.get(anime.getAnimeId())).thenReturn(anime);

        catalogService.updateAnime(anime);

        verify(cacheStore).evict(anime.getAnimeId());
        verify(catalogRepository).existsById(anime.getAnimeId());
        verify(catalogRepository).save(argumentCaptor.capture());
        CatalogRecord capturedAnimeRecord = argumentCaptor.getValue();
        assertEquals(capturedAnimeRecord.getTitle(), animeCatalogRecord.getTitle());
        assertEquals(capturedAnimeRecord.getAnimeId(), animeCatalogRecord.getAnimeId());
        assertEquals(capturedAnimeRecord.getDescription(), animeCatalogRecord.getDescription());
        assertEquals(capturedAnimeRecord.getImage(), animeCatalogRecord.getImage());
        assertEquals(capturedAnimeRecord.getStartDate(), animeCatalogRecord.getStartDate());
        assertEquals(capturedAnimeRecord.getSeason(), animeCatalogRecord.getSeason());
        assertEquals(capturedAnimeRecord.getPopularity(), animeCatalogRecord.getPopularity());
        assertEquals(capturedAnimeRecord.getRating(), animeCatalogRecord.getRating());
        assertEquals(capturedAnimeRecord.getEpisodes(), animeCatalogRecord.getEpisodes());
        assertEquals(capturedAnimeRecord.getGenre(), animeCatalogRecord.getGenre());

    }

    @Test
    public void updateAnime_descriptionUpdated() {
        String animeId = randomUUID().toString();
        String description = mockNeat.strings().val();

        Anime anime = new Anime("Title", animeId, description,
                "image", 2007, "season",300, 90, 500, new ArrayList<>());

        when(cacheStore.get(anime.getDescription())).thenReturn(anime);

        catalogService.updateAnime(anime);
    }

    @Test
    public void findAllAnime(){
        CatalogRecord anime1 = new CatalogRecord();
        anime1.setTitle("Title 1");
        anime1.setAnimeId(randomUUID().toString());
        anime1.setDescription("Description 1");
        anime1.setImage("Image 1");
        anime1.setStartDate(2023);
        anime1.setSeason("Season 1");
        anime1.setPopularity(500);
        anime1.setRating(99);
        anime1.setEpisodes(19);
        anime1.setGenre(new ArrayList<>());



        CatalogRecord anime2 = new CatalogRecord();
        anime2.setTitle("Title 2");
        anime2.setAnimeId(randomUUID().toString());
        anime2.setDescription("Description 2");
        anime2.setImage("Image 2");
        anime2.setStartDate(2022);
        anime2.setSeason("Season 2");
        anime2.setPopularity(400);
        anime2.setRating(98);
        anime2.setEpisodes(7);
        anime2.setGenre(new ArrayList<>());

        List<CatalogRecord> records = new ArrayList<>();

        records.add(anime1);
        records.add(anime2);

        when(catalogRepository.findAll()).thenReturn(records);

        List<Anime> recordList = catalogService.findAllAnime();

        Assertions.assertNotNull(recordList, "The list of anime is returned");
        Assertions.assertEquals(2, recordList.size(), "Here are 2 animes");

        for(Anime anime : recordList) {
            if(anime.getAnimeId() == anime1.getAnimeId()){
                Assertions.assertEquals(anime1.getTitle(), anime.getTitle(), "The anime title matches");
                Assertions.assertEquals(anime1.getDescription(), anime.getDescription(), "The anime description matches");
                Assertions.assertEquals(anime1.getImage(), anime.getImage(), "The anime image matches");
                Assertions.assertEquals(anime1.getStartDate(), anime.getStartDate(), "The anime start date matches");
                Assertions.assertEquals(anime1.getSeason(), anime.getSeason(), "The anime season matches");
                Assertions.assertEquals(anime1.getPopularity(), anime.getPopularity(), "The anime popularity matches");
                Assertions.assertEquals(anime1.getRating(), anime.getRating(), "The anime rating matches");
                Assertions.assertEquals(anime1.getEpisodes(), anime.getEpisodes(), "The anime episodes match");
                Assertions.assertEquals(anime1.getGenre(), anime.getGenre(), "The anime genre matches");

            } else if(anime.getAnimeId() == anime2.getAnimeId()){
                Assertions.assertEquals(anime2.getTitle(), anime.getTitle(), "The anime title matches");
                Assertions.assertEquals(anime2.getDescription(), anime.getDescription(), "The anime description matches");
                Assertions.assertEquals(anime2.getImage(), anime.getImage(), "The anime image matches");
                Assertions.assertEquals(anime2.getStartDate(), anime.getStartDate(), "The anime start date matches");
                Assertions.assertEquals(anime2.getSeason(), anime.getSeason(), "The anime season matches");
                Assertions.assertEquals(anime2.getPopularity(), anime.getPopularity(), "The anime popularity matches");
                Assertions.assertEquals(anime2.getRating(), anime.getRating(), "The anime rating matches");
                Assertions.assertEquals(anime2.getEpisodes(), anime.getEpisodes(), "The anime episodes match");
                Assertions.assertEquals(anime2.getGenre(), anime.getGenre(), "The anime genre matches");
            }else {
                Assertions.assertTrue(false, "Anime returned is not in the records!");
            }
        }
    }

    @Test
    void testGetSeasonAnime() {
        PaginatedQueryList<CatalogRecord> mockResult = mock(PaginatedQueryList.class);
        when(mapper.query(any(Class.class), any(DynamoDBQueryExpression.class))).thenReturn(mockResult);
        PaginatedQueryList<CatalogRecord> result = catalogService.getSeasonAnime();
        verify(mapper).query(any(Class.class), any(DynamoDBQueryExpression.class));
        assertEquals(mockResult, result);
    }

}
