package com.kenzie.appserver;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.kenzie.appserver.DAO.GraphQLResponse;
import com.kenzie.appserver.DAO.Media;
import com.kenzie.appserver.config.CacheAnimeStore;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.service.CatalogService;
import com.kenzie.appserver.service.model.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Main {
    public static String fileName = "C:\\Users\\micha\\kenzie\\ata-capstone-project-blakepf\\Application\\src\\main\\java\\com\\kenzie\\appserver\\request.txt";
    public static Path filePath = Path.of(fileName);
    public static final String graphUri = "https://graphql.anilist.co";

    public static CatalogService catalogService;

    public static CatalogRepository catalogRepository;

    public static CacheAnimeStore animeStore;

    @Autowired
    public Main(CatalogService catalogService, CatalogRepository catalogRepository, CacheAnimeStore cacheAnimeStore){
        Main.catalogService = new CatalogService(catalogRepository, cacheAnimeStore);
    }



    static ObjectMapper objectMapper = new ObjectMapper();


    public static void main(String[] args) {
        try {
            String data = makePostRequest();
            System.out.println(data);

            GraphQLResponse response = objectMapper.readValue(data, GraphQLResponse.class);

            List<Media> mediaList = response.getData().getPage().getMedia();

            for (Media media : mediaList) {
                Anime anime = new Anime(media.getTitle().getUserPreferred(), String.valueOf(media.getId()),
                        media.getDescription(), media.getCoverImage().getMedium(),
                        media.getStartDate().getYear(), media.getSeason(),
                        media.getPopularity(), media.getAverageScore(),
                        media.getEpisodes(), media.getGenres());

                catalogService.addNewAnime(anime);
            }

            } catch(InterruptedException e){
                throw new RuntimeException(e);
            } catch(IOException e){
                throw new RuntimeException(e);
            } catch(URISyntaxException e){
                throw new RuntimeException(e);
            }

    }
    public static String makePostRequest() throws IOException, URISyntaxException, InterruptedException {

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
            System.out.println("Response body: " + responseBody);

            // Process the response as needed
            // ...
        } else {
            System.err.println("Request failed with HTTP error code: " + statusCode + " " + responseBody);
        }
        return responseBody;
    }
}

