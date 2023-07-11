package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
import com.kenzie.appserver.repositories.ReviewRepository;
import com.kenzie.appserver.repositories.model.ReviewRecord;
import com.kenzie.appserver.service.model.Review;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;

import java.util.*;

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

        when(mapper.query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mock(PaginatedQueryList.class, withSettings().defaultAnswer(new ForwardsInvocations(reviewRecordList))));

        PaginatedQueryList<ReviewRecord> result = reviewService.getListOfAnimeByDisplayName("Hohoho");

        verify(mapper).query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class));
        assertEquals(2,result.size());
    }

    @Test
    public void calculate_null(){
        List<ReviewRecord> reviewRecordList = new ArrayList<>();

        when(mapper.query(eq(ReviewRecord.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(mock(PaginatedQueryList.class, withSettings().defaultAnswer(new ForwardsInvocations(reviewRecordList))));

        Integer data = reviewService.calculateAverageRatingByAnime(1);

        Assertions.assertNull(data);
    }

    @Test
    public void getReviewsForAnime_withID_LastKey(){
        Map<String, AttributeValue> exclusiveKey = new HashMap<>();
        exclusiveKey.put("animeID", new AttributeValue().withN("1"));
        exclusiveKey.put("postDate", new AttributeValue().withN("0"));
        exclusiveKey.put("reviewID", new AttributeValue("string"));

        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setPostDate(20230102);
        reviewRecord.setDisplayName("test");
        reviewRecord.setAnimeID(1);
        reviewRecord.setRating(99);
        reviewRecord.setReviewID("test-id");
        reviewRecord.setReview("Cool");

        ReviewRecord reviewRecord2 = new ReviewRecord();
        reviewRecord2.setPostDate(20230103);
        reviewRecord2.setDisplayName("test-2");
        reviewRecord2.setAnimeID(1);
        reviewRecord2.setRating(12);
        reviewRecord2.setReviewID("test-id2");
        reviewRecord2.setReview("Awesome");

        List<ReviewRecord> reviewRecordList = new ArrayList<>(Arrays.asList(reviewRecord, reviewRecord2));

        when(mapper.queryPage(eq(ReviewRecord.class),
                any(DynamoDBQueryExpression.class)))
                .thenReturn(mock(QueryResultPage.class,
                        withSettings().defaultAnswer(new ForwardsInvocations(reviewRecordList))));


        QueryResultPage<ReviewRecord> page = reviewService.getReviewsForAnime(1, exclusiveKey);

        Assertions.assertNotNull(page);
    }

}