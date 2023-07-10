package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.reviewModels.GetReviewsRequest;
import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
import com.kenzie.appserver.service.NotificationService;
import com.kenzie.appserver.service.ReviewService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@IntegrationTest
public class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ReviewService reviewService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPostReview() throws Exception {
        // Create a mock user review request
        UserReviewPostRequest userReviewPostRequest = new UserReviewPostRequest();
        // Set the required fields of the user review request

        // Convert the user review request to JSON
        String requestJson = mapper.writeValueAsString(userReviewPostRequest);

        // Perform the POST request and validate the response
        mvc.perform(MockMvcRequestBuilders.post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewID").exists())
                // Validate other fields in the response
                .andReturn();
    }

//    @Test
//    public void testDeleteReviewByID() throws Exception {
//        // Create a review ID for testing
//        String reviewID = "exampleReviewID";
//
//        // Perform the DELETE request and validate the response
//        mvc.perform(delete("/review/{reviewID}", reviewID))
//                .andExpect(MockMvcResultMatchers.status().isNoContent())
//                .andReturn();
//    }

//    @Test
//    public void testReviewsFromAnimeID() throws Exception {
//        // Create a mock get reviews request
//        GetReviewsRequest getReviewsRequest = new GetReviewsRequest();
//        // Set the required fields of the get reviews request
//
//        // Convert the get reviews request to JSON
//        String requestJson = mapper.writeValueAsString(getReviewsRequest);
//
//        // Perform the POST request and validate the response
//        mvc.perform(MockMvcRequestBuilders.post("/review/limit")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
//                // Validate other fields in the response
//                .andReturn();
//    }

    @Test
    public void testCalculateAverageScore() throws Exception {
        // Create a mock anime ID for testing
        int animeID = 123;

        // Perform the GET request and validate the response
        mvc.perform(MockMvcRequestBuilders.get("/review/{animeID}", animeID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNumber())
                .andReturn();
    }

    @Test
    public void testGetReviewsByDisplayName() throws Exception {
        // Create a mock display name for testing
        String displayName = "exampleDisplayName";

        // Perform the GET request and validate the response
        mvc.perform(MockMvcRequestBuilders.get("/review/username/{displayName}", displayName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andReturn();
    }
}
