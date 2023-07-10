package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.reviewModels.GetReviewsRequest;
import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
import com.kenzie.appserver.service.NotificationService;
import com.kenzie.appserver.service.ReviewService;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.Review;
import com.kenzie.appserver.service.model.User;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ReviewService reviewService;

    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testPostReview() throws Exception {
        UserReviewPostRequest userReviewPostRequest = new UserReviewPostRequest();

        String requestJson = mapper.writeValueAsString(userReviewPostRequest);

        mvc.perform(MockMvcRequestBuilders.post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewID").exists())
                .andReturn();
    }

    @Test
    public void getReview_ReviewDoesNotExist() throws Exception {
        String reviewId = UUID.randomUUID().toString();
        mvc.perform(get("/review/{reviewID}", reviewId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void testCalculateAverageScore() throws Exception {
        int animeID = 123;

        mvc.perform(MockMvcRequestBuilders.get("/review/{animeID}", animeID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNumber())
                .andReturn();
    }

    @Test
    public void testGetReviewsByDisplayName() throws Exception {
        String displayName = "exampleDisplayName";

        mvc.perform(MockMvcRequestBuilders.get("/review/username/{displayName}", displayName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andReturn();
    }
}
