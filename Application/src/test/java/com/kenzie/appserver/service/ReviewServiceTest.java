//package com.kenzie.appserver.service;
//
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
//import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
//import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
//import com.kenzie.appserver.controller.reviewModels.ReviewResponse;
//import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
//import com.kenzie.appserver.repositories.ReviewRepository;
//import com.kenzie.appserver.repositories.model.ReviewRecord;
//import com.kenzie.appserver.service.model.Review;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//public class ReviewServiceTest {
//    private ReviewService reviewService;
//    private ReviewRepository reviewRepository;
//    private DynamoDBMapper mapper;
//
//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.initMocks(this);
//        mapper = mock(DynamoDBMapper.class);
//        reviewRepository = mock(ReviewRepository.class);
//        reviewService = new ReviewService(reviewRepository, mapper);
//    }
//
//
//    @Test
//    public void post_validInputs_returnCreatedReview(){
//        UserReviewPostRequest request = new UserReviewPostRequest();
//        request.setReview("testReview");
//        request.setRating(100);
//        request.setDisplayName("testUser");
//        request.setAnimeID(1);
//
//        Review review = reviewService.postReview(request);
//
//        Assertions.assertEquals(100, review.getRating());
//        Assertions.assertEquals("testReview", review.getReview());
//        Assertions.assertEquals(1, review.getAnimeID());
//        Assertions.assertEquals("testUser", review.getDisplayName());
//
//    }
//
//    @Test
//    public void delete_anyInput_callsRepository(){
//        String uuid = "testUser";
//
//        reviewService.deleteReview(uuid);
//
//        verify(reviewRepository, times(1)).deleteById(uuid);
//    }
//
//    @Test
//    public void findReview_validID_returnsReview(){
//        UserReviewPostRequest request = new UserReviewPostRequest();
//        request.setReview("testReview");
//        request.setRating(100);
//        request.setDisplayName("testUser");
//        request.setAnimeID(1);
//
//        Review createdReview = reviewService.postReview(request);
//
//        ReviewRecord reviewRecord = new ReviewRecord();
//        reviewRecord.setReviewID(createdReview.getReviewID());
//        reviewRecord.setRating(createdReview.getRating());
//        reviewRecord.setUserID(createdReview.getDisplayName());
//        reviewRecord.setAnimeID(createdReview.getAnimeID());
//        reviewRecord.setReview(createdReview.getReview());
//        reviewRecord.setPostDate(createdReview.getPostDate());
//
//        when(reviewRepository.findById(createdReview.getReviewID())).thenReturn(Optional.of(reviewRecord));
//        Review foundReview = reviewService.findReview(createdReview.getReviewID());
//
//        Assertions.assertEquals(createdReview.getReviewID(), foundReview.getReviewID());
//
//    }
//
//    @Test
//    public void findReview_invalidID_returnsReview() {
//        String reviewID = "id does not exist";
//
//        when(reviewRepository.findById(reviewID)).thenReturn(Optional.ofNullable(null));
//        Review notFoundReview = reviewService.findReview(reviewID);
//
//        Assertions.assertNull(notFoundReview);
//    }
//
//    @Test
//    public void getReviewsForAnime_validAnime_ReturnsPageOfReviews(){
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
//        Review createdReview = reviewService.postReview(request);
//        Review createdReview2 = reviewService.postReview(request2);
//
//        ReviewRecord reviewRecord = new ReviewRecord();
//        reviewRecord.setPostDate(createdReview.getPostDate());
//        reviewRecord.setUserID(createdReview.getDisplayName());
//        reviewRecord.setAnimeID(createdReview.getAnimeID());
//        reviewRecord.setRating(createdReview.getRating());
//        reviewRecord.setReviewID(createdReview.getReviewID());
//        reviewRecord.setReview(createdReview.getReview());
//
//        ReviewRecord reviewRecord2 = new ReviewRecord();
//        reviewRecord.setPostDate(createdReview2.getPostDate());
//        reviewRecord.setUserID(createdReview2.getDisplayName());
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
//        QueryResultPage<ReviewRecord> resultPage = reviewService.getReviewsForAnime(1);
//        Assertions.assertNotNull(resultPage);
//    }
//
//
//
//
//
//}
