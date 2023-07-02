package com.kenzie.appserver;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.DAO.GraphQLResponse;
import com.kenzie.appserver.DAO.Media;
import com.kenzie.appserver.config.CacheAnimeStore;
import com.kenzie.appserver.config.DynamoDbConfig;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.service.CatalogService;
import com.kenzie.appserver.service.model.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class Application {
    public static String fileName = "C:\\Users\\micha\\kenzie\\ata-capstone-project-blakepf\\Application\\src\\main\\java\\com\\kenzie\\appserver\\popularAnimeRequest.txt";
    public static Path filePath = Path.of(fileName);
    public static final String graphUri = "https://graphql.anilist.co";

    public static CatalogService catalogService;

    public  CatalogRepository catalogRepository;

    public  CacheAnimeStore animeStore;

    private DynamoDBMapper mapper;

    @Autowired
    public Application(CatalogRepository catalogRepository, CacheAnimeStore cacheAnimeStore, DynamoDBMapper mapper){
       this.catalogService = new CatalogService(catalogRepository, cacheAnimeStore, mapper);
    }



    static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        try {
            Path seasonalAnime = Path.of("C:\\Users\\micha\\kenzie\\ata-capstone-project-blakepf\\Application\\src\\main\\java\\com\\kenzie\\appserver\\graphqlrequests\\seasonalAnimeRequest.txt");
            Path popularAnime = Path.of("C:\\Users\\micha\\kenzie\\ata-capstone-project-blakepf\\Application\\src\\main\\java\\com\\kenzie\\appserver\\graphqlrequests\\popularAnimeRequest.txt");
            Path highlyRated = Path.of("C:\\Users\\micha\\kenzie\\ata-capstone-project-blakepf\\Application\\src\\main\\java\\com\\kenzie\\appserver\\graphqlrequests\\highlyRatedAnimeRequest.txt");


            List<String> dataResponse = new ArrayList<>();
            dataResponse.add(makePostRequest(popularAnime));
            dataResponse.add(makePostRequest(seasonalAnime));
            dataResponse.add(makePostRequest(highlyRated));

            List<GraphQLResponse> graphQLResponsesList = new ArrayList<>();

            for(String response : dataResponse){
                graphQLResponsesList.add(objectMapper.readValue(response, GraphQLResponse.class));
            }

            List<List<Media>> mediaLists = new ArrayList<>();
            for(GraphQLResponse graphQLResponse : graphQLResponsesList){
                mediaLists.add(graphQLResponse.getData().getPage().getMedia());
            }



            for(List<Media> mediaList : mediaLists){
                for (Media media : mediaList) {
                Anime anime = new Anime(media.getTitle().getUserPreferred(), String.valueOf(media.getId()),
                        media.getDescription(), media.getCoverImage().getLarge(),
                        media.getStartDate().getYear(), media.getSeason(),
                        media.getPopularity(), media.getAverageScore(),
                        media.getEpisodes(), media.getGenres());

                catalogService.addNewAnime(anime);
            }
        }
            System.out.println("Tables have been Filled");

        } catch(InterruptedException e){
            throw new RuntimeException(e);
        } catch(IOException e){
            throw new RuntimeException(e);
        } catch(URISyntaxException e){
            throw new RuntimeException(e);
        }

    }
    public static String makePostRequest(Path filePath) throws IOException, URISyntaxException, InterruptedException {

        // GraphQL query

        // Variables (if any)
        Map<String, Object> variables = new HashMap<>();

        // Build the GraphQL request payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", Files.readString(filePath));
        requestBody.put("variables", variables);

        // Create an ObjectMapper for JSON serialization

        // Convert the request payload to JSON
        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create an HttpRequest with the JSON payload
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(graphUri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send the HTTP request and process the response
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        String responseBody = response.body();

        if (statusCode == 200) {
            // Process the response as needed
            // ...
        } else {
            System.err.println("Request failed with HTTP error code: " + statusCode + " " + responseBody);
        }
        return responseBody;
    }
}

