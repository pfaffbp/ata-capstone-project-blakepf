package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kenzie.appserver.controller.reviewModels.ReviewResponse;
import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
import com.kenzie.appserver.repositories.ReviewRepository;
import com.kenzie.appserver.repositories.model.ReviewRecord;
import com.kenzie.appserver.service.model.Review;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {
    private ReviewService reviewService;
    private ReviewRepository reviewRepository;
    private DynamoDBMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mapper = mock(DynamoDBMapper.class);
        reviewRepository = mock(ReviewRepository.class);
        reviewService = new ReviewService(reviewRepository, mapper);
    }

    @Test
    void postReview() {
        UserReviewPostRequest request = new UserReviewPostRequest();
        request.setAnimeID(50);
        request.setDisplayName("displayName");
        request.setRating(90);
        request.setReview("Great anime");

        ArgumentCaptor<ReviewRecord> reviewCaptor = ArgumentCaptor.forClass(ReviewRecord.class);

        Review review = reviewService.postReview(request);

        Assertions.assertNotNull(review);

        verify(reviewRepository).save(reviewCaptor.capture());

        ReviewRecord capturedReview = reviewCaptor.getValue();

        Assertions.assertNotNull(capturedReview, "The review record is returned");
        assertEquals(capturedReview.getAnimeID(), request.getAnimeID());
        assertEquals(capturedReview.getDisplayName(), request.getDisplayName());
        assertEquals(capturedReview.getRating(), request.getRating());
        assertEquals(capturedReview.getReview(), request.getReview());
    }


    @Test
    public void post_validInputs_returnCreatedReview() {
        UserReviewPostRequest request = new UserReviewPostRequest();
        request.setReview("testReview");
        request.setRating(100);
        request.setDisplayName("testUser");
        request.setAnimeID(1);

        Review review = reviewService.postReview(request);

        Assertions.assertEquals(100, review.getRating());
        Assertions.assertEquals("testReview", review.getReview());
        Assertions.assertEquals(1, review.getAnimeID());
        Assertions.assertEquals("testUser", review.getDisplayName());

    }

    @Test
    public void delete_anyInput_callsRepository() {
        String uuid = "testUser";

        reviewService.deleteReview(uuid);

        verify(reviewRepository, times(1)).deleteById(uuid);
    }

    @Test
    public void findReview_validID_returnsReview() {
        UserReviewPostRequest request = new UserReviewPostRequest();
        request.setReview("testReview");
        request.setRating(100);
        request.setDisplayName("testUser");
        request.setAnimeID(1);

        Review createdReview = reviewService.postReview(request);

        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setReviewID(createdReview.getReviewID());
        reviewRecord.setRating(createdReview.getRating());
        reviewRecord.setDisplayName(createdReview.getDisplayName());
        reviewRecord.setAnimeID(createdReview.getAnimeID());
        reviewRecord.setReview(createdReview.getReview());
        reviewRecord.setPostDate(createdReview.getPostDate());

        when(reviewRepository.findById(createdReview.getReviewID())).thenReturn(Optional.of(reviewRecord));
        Review foundReview = reviewService.findReview(createdReview.getReviewID());

        Assertions.assertEquals(createdReview.getReviewID(), foundReview.getReviewID());

    }

    @Test
    public void findReview_invalidID_returnsReview() {
        String reviewID = "id does not exist";

        when(reviewRepository.findById(reviewID)).thenReturn(Optional.ofNullable(null));
        Review notFoundReview = reviewService.findReview(reviewID);

        Assertions.assertNull(notFoundReview);
    }

    @Test
    public void getReviewsForAnime_validAnime_ReturnsPageOfReviews() {
        UserReviewPostRequest request = new UserReviewPostRequest();
        request.setReview("testReview");
        request.setRating(100);
        request.setDisplayName("testUser");
        request.setAnimeID(1);

        UserReviewPostRequest request2 = new UserReviewPostRequest();
        request.setReview("testReview2");
        request.setRating(94);
        request.setDisplayName("testUser2");
        request.setAnimeID(1);

        Review createdReview = reviewService.postReview(request);
        Review createdReview2 = reviewService.postReview(request2);

        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setPostDate(createdReview.getPostDate());
        reviewRecord.setDisplayName(createdReview.getDisplayName());
        reviewRecord.setAnimeID(createdReview.getAnimeID());
        reviewRecord.setRating(createdReview.getRating());
        reviewRecord.setReviewID(createdReview.getReviewID());
        reviewRecord.setReview(createdReview.getReview());

        ReviewRecord reviewRecord2 = new ReviewRecord();
        reviewRecord.setPostDate(createdReview2.getPostDate());
        reviewRecord.setDisplayName(createdReview2.getDisplayName());
        reviewRecord.setAnimeID(createdReview2.getAnimeID());
        reviewRecord.setRating(createdReview2.getRating());
        reviewRecord.setReviewID(createdReview2.getReviewID());
        reviewRecord.setReview(createdReview2.getReview());

        List<ReviewRecord> reviewRecordList = new ArrayList<>(Arrays.asList(reviewRecord, reviewRecord2));


        when(mapper.queryPage(eq(ReviewRecord.class),
                any(DynamoDBQueryExpression.class)))
                .thenReturn(mock(QueryResultPage.class,
                        withSettings().defaultAnswer(new ForwardsInvocations(reviewRecordList))));

        QueryResultPage<ReviewRecord> resultPage = reviewService.getReviewsForAnime(1);
        Assertions.assertNotNull(resultPage);
    }

//    @Test
//    void getReviewsForAnime_shouldQueryMapperWithCorrectParameters() {
//        int animeId = 123;
//        String currentDate = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
//
//        QueryResultPage<ReviewRecord> expectedPage = new QueryResultPage<>();
//        when(mapper.queryPage(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
//                .thenReturn(expectedPage);
//
//        QueryResultPage<ReviewRecord> result = reviewService.getReviewsForAnime(animeId);
//
//        Map<String, AttributeValue> expectedValueMap = new HashMap<>();
//        expectedValueMap.put(":animeID", new AttributeValue().withN(String.valueOf(animeId)));
//        expectedValueMap.put(":postDate", new AttributeValue().withN(currentDate));
//
//        DynamoDBQueryExpression<ReviewRecord> expectedExpression =
//                new DynamoDBQueryExpression<ReviewRecord>()
//                        .withIndexName("REVIEW_lOOK_UP")
//                        .withConsistentRead(false)
//                        .withLimit(10)
//                        .withKeyConditionExpression("animeID = :animeID and postDate <= :postDate")
//                        .withExpressionAttributeValues(expectedValueMap);
//
//        verify(mapper).queryPage(eq(ReviewRecord.class), eq(expectedExpression));
//        Assertions.assertEquals(expectedPage, result);
//    }
//
//    @Test
//    void getReviewsForAnime_withLastKey_shouldQueryMapperWithExclusiveStartKey() {
//        int animeId = 123;
//        String currentDate = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
//        Map<String, AttributeValue> lastKey = new HashMap<>();
//        lastKey.put("postDate", new AttributeValue().withN("0"));
//
//        QueryResultPage<ReviewRecord> expectedPage = new QueryResultPage<>();
//        when(mapper.queryPage(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
//                .thenReturn(expectedPage);
//
//        QueryResultPage<ReviewRecord> result = reviewService.getReviewsForAnime(animeId, lastKey);
//
//        Map<String, AttributeValue> expectedValueMap = new HashMap<>();
//        expectedValueMap.put(":animeID", new AttributeValue().withN(String.valueOf(animeId)));
//        expectedValueMap.put(":postDate", new AttributeValue().withN(currentDate));
//
//        DynamoDBQueryExpression<ReviewRecord> expectedExpression =
//                new DynamoDBQueryExpression<ReviewRecord>()
//                        .withIndexName("REVIEW_lOOK_UP")
//                        .withConsistentRead(false)
//                        .withLimit(10)
//                        .withExclusiveStartKey(lastKey)
//                        .withKeyConditionExpression("animeID = :animeID and postDate <= :postDate")
//                        .withExpressionAttributeValues(expectedValueMap);
//
//        verify(mapper).queryPage(eq(ReviewRecord.class), eq(expectedExpression));
//        Assertions.assertSame(expectedPage, result);
//    }

    @Test
    void calculateAverageRatingByAnime_shouldReturnNullIfNoReviewRecords() {
        when(mapper.query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mock(PaginatedQueryList.class, withSettings().defaultAnswer(new ForwardsInvocations(null))));

        Integer result = reviewService.calculateAverageRatingByAnime(1);

        Assertions.assertNull(result);
    }

    @Test
    void calculateAverageRatingByAnime() {

        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setPostDate(15);
        reviewRecord.setDisplayName("Hohoho");
        reviewRecord.setAnimeID(52);
        reviewRecord.setRating(99);
        reviewRecord.setReviewID("58");
        reviewRecord.setReview("Great!");

        ReviewRecord reviewRecord2 = new ReviewRecord();
        reviewRecord2.setPostDate(14);
        reviewRecord2.setDisplayName("Hoho");
        reviewRecord2.setAnimeID(52);
        reviewRecord2.setRating(85);
        reviewRecord2.setReviewID("65");
        reviewRecord2.setReview("Cool!");

        List<ReviewRecord> reviewRecordList = new ArrayList<>(Arrays.asList(reviewRecord, reviewRecord2));

        when(mapper.query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mock(PaginatedQueryList.class, withSettings().defaultAnswer(new ForwardsInvocations(reviewRecordList))));
//        when(mapper.query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
//                .thenReturn(emptyList);

        Integer result = reviewService.calculateAverageRatingByAnime(52);

        Assertions.assertEquals(92, result);
    }

    @Test
    void testGetListOfAnimeByDisplayName() {

        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setPostDate(15);
        reviewRecord.setDisplayName("Hohoho");
        reviewRecord.setAnimeID(52);
        reviewRecord.setRating(99);
        reviewRecord.setReviewID("58");
        reviewRecord.setReview("Great!");

        ReviewRecord reviewRecord2 = new ReviewRecord();
        reviewRecord2.setPostDate(14);
        reviewRecord2.setDisplayName("Hohoho");
        reviewRecord2.setAnimeID(52);
        reviewRecord2.setRating(85);
        reviewRecord2.setReviewID("65");
        reviewRecord2.setReview("Cool!");

        List<ReviewRecord> reviewRecordList = new ArrayList<>(Arrays.asList(reviewRecord, reviewRecord2));

        // Set up the mock behavior
        when(mapper.query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mock(PaginatedQueryList.class, withSettings().defaultAnswer(new ForwardsInvocations(reviewRecordList))));

        // Call the method under test
        PaginatedQueryList<ReviewRecord> result = reviewService.getListOfAnimeByDisplayName("Hohoho");

        // Verify the interactions and assertions
        verify(mapper).query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class));
        assertEquals(2,result.size());
    }
}

//    @Test
//    void testGetReviewsForAnime() {
//        // Mock data
//        int animeId = 1;
//        Map<String, AttributeValue> valueMap = new HashMap<>();
//        // Set up the mock behavior
//        when(mapper.queryPage(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
//                .thenReturn(new QueryResultPage<>());
//
//        // Call the method under test
//        QueryResultPage<ReviewRecord> result = reviewService.getReviewsForAnime(animeId);
//
//        // Verify the interactions and assertions
//        verify(mapper).queryPage(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class));
//        assertEquals(new QueryResultPage<>(), result);
//    }

//    @Test
//    void testCalculateAverageRatingByAnime() {
//        // Mock data
//        int animeId = 1;
//        Map<String, AttributeValue> valueMap = new HashMap<>();
//        // Set up the mock behavior
//        when(mapper.query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
//                .thenReturn(new PaginatedQueryList<>());
//
//        // Call the method under test
//        Integer result = reviewService.calculateAverageRatingByAnime(animeId);
//
//        // Verify the interactions and assertions
//        verify(mapper).query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class));
//        assertEquals(null, result);
//    }

//    @Test
//    void testGetListOfAnimeByDisplayName() {
//        // Mock data
//        String displayName = "John Doe";
//        Map<String, AttributeValue> valueMap = new HashMap<>();
//        // Set up the mock behavior
//        when(mapper.query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
//                .thenReturn(new PaginatedQueryList<>());
//
//        // Call the method under test
//        PaginatedQueryList<ReviewRecord> result = reviewService.getListOfAnimeByDisplayName(displayName);
//
//        // Verify the interactions and assertions
//        verify(mapper).query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class));
//        assertEquals(new PaginatedQueryList<>(), result);
//    }
