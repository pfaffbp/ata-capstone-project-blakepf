package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
import com.kenzie.appserver.repositories.ReviewRepository;
import com.kenzie.appserver.repositories.model.ReviewRecord;
import com.kenzie.appserver.service.model.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


import java.util.*;

import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    private ReviewRepository repository;
    private DynamoDBMapper mapper;
    private ReviewService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        mapper = mock(DynamoDBMapper.class);
        repository = mock(ReviewRepository.class);
        service = new ReviewService(repository, mapper);
    }

    @Test
    void postReview() {
        UserReviewPostRequest request = new UserReviewPostRequest();
        request.setAnimeID(50);
        request.setDisplayName("displayName");
        request.setRating(90);
        request.setReview("Great anime");

        ArgumentCaptor<ReviewRecord> reviewCaptor = ArgumentCaptor.forClass(ReviewRecord.class);

        Review review = service.postReview(request);

        Assertions.assertNotNull(review);

        verify(repository).save(reviewCaptor.capture());

        ReviewRecord capturedReview = reviewCaptor.getValue();

        Assertions.assertNotNull(capturedReview, "The review record is returned");
        assertEquals(capturedReview.getAnimeID(), request.getAnimeID());
        assertEquals(capturedReview.getDisplayName(), request.getDisplayName());
        assertEquals(capturedReview.getRating(), request.getRating());
        assertEquals(capturedReview.getReview(), request.getReview());
    }

    @Test
    public void delete_anyInput_callsRepository() {
        String uuid = "testUser";

        service.deleteReview(uuid);

        verify(repository, times(1)).deleteById(uuid);
    }

    @Test
    public void post_validInputs_returnCreatedReview() {
        UserReviewPostRequest request = new UserReviewPostRequest();
        request.setReview("testReview");
        request.setRating(100);
        request.setDisplayName("testUser");
        request.setAnimeID(1);

        Review review = service.postReview(request);

        Assertions.assertEquals(100, review.getRating());
        Assertions.assertEquals("testReview", review.getReview());
        Assertions.assertEquals(1, review.getAnimeID());
        Assertions.assertEquals("testUser", review.getDisplayName());

    }

    @Test
    public void findReview_validID_returnsReview() {
        UserReviewPostRequest request = new UserReviewPostRequest();
        request.setReview("testReview");
        request.setRating(100);
        request.setDisplayName("testUser");
        request.setAnimeID(1);

        Review createdReview = service.postReview(request);

        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setReviewID(createdReview.getReviewID());
        reviewRecord.setRating(createdReview.getRating());
        reviewRecord.setDisplayName(createdReview.getDisplayName());
        reviewRecord.setAnimeID(createdReview.getAnimeID());
        reviewRecord.setReview(createdReview.getReview());
        reviewRecord.setPostDate(createdReview.getPostDate());

        when(repository.findById(createdReview.getReviewID())).thenReturn(Optional.of(reviewRecord));
        Review foundReview = service.findReview(createdReview.getReviewID());

        Assertions.assertEquals(createdReview.getReviewID(), foundReview.getReviewID());

    }

    @Test
    public void findReview_invalidID_returnsReview() {
        String reviewID = "id does not exist";

        when(repository.findById(reviewID)).thenReturn(Optional.ofNullable(null));
        Review notFoundReview = service.findReview(reviewID);

        Assertions.assertNull(notFoundReview);
    }

//    @Test
//    public void getReviewsForAnime_validAnime_ReturnsPageOfReviews() {
//        UserReviewPostRequest request = new UserReviewPostRequest();
//        request.setReview("testReview");
//        request.setRating(100);
//        request.setDisplayName("testUser");
//        request.setAnimeID(1);
//
//        UserReviewPostRequest request2 = new UserReviewPostRequest();
//        request.setReview("testReview2");
//        request.setRating(94);
//        request.setDisplayName("testUser2");
//        request.setAnimeID(1);
//
//        Review createdReview = service.postReview(request);
//        Review createdReview2 = service.postReview(request2);
//
//        ReviewRecord reviewRecord = new ReviewRecord();
//        reviewRecord.setPostDate(createdReview.getPostDate());
//        reviewRecord.setDisplayName(createdReview.getDisplayName());
//        reviewRecord.setAnimeID(createdReview.getAnimeID());
//        reviewRecord.setRating(createdReview.getRating());
//        reviewRecord.setReviewID(createdReview.getReviewID());
//        reviewRecord.setReview(createdReview.getReview());
//
//        ReviewRecord reviewRecord2 = new ReviewRecord();
//        reviewRecord.setPostDate(createdReview2.getPostDate());
//        reviewRecord.setDisplayName(createdReview2.getDisplayName());
//        reviewRecord.setAnimeID(createdReview2.getAnimeID());
//        reviewRecord.setRating(createdReview2.getRating());
//        reviewRecord.setReviewID(createdReview2.getReviewID());
//        reviewRecord.setReview(createdReview2.getReview());
//
//        List<ReviewRecord> reviewRecordList = new ArrayList<>(Arrays.asList(reviewRecord, reviewRecord2));
//
//
//        when(mapper.queryPage(eq(ReviewRecord.class),
//                any(DynamoDBQueryExpression.class)))
//                .thenReturn(mock(QueryResultPage.class,
//                        withSettings().defaultAnswer(new ForwardsInvocations(reviewRecordList))));
//
//        QueryResultPage<ReviewRecord> resultPage = service.getReviewsForAnime(1);
//        Assertions.assertNotNull(resultPage);
//    }

}


