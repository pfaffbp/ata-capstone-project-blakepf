//package com.kenzie.appserver.service;
//
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
//import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
//import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//import com.kenzie.appserver.config.CacheAnimeStore;
//import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
//import com.kenzie.appserver.repositories.ReviewRepository;
//import com.kenzie.appserver.repositories.model.ReviewRecord;
//import com.kenzie.appserver.service.model.Review;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.mockito.Mockito.*;
//import static org.testcontainers.shaded.org.hamcrest.Matchers.any;
//
//public class ReviewServiceTest {
//
//    private ReviewRepository repository;
//}
//
////    private DynamoDBMapper mapper;
////    private ReviewService service;
////
////    @BeforeEach
////    void setup() {
////        MockitoAnnotations.initMocks(this);
////        mapper = mock(DynamoDBMapper.class);
////        repository = mock(ReviewRepository.class);
////        service = new ReviewService(repository, mapper);
////    }
////
////    @Test
////    void postReview() {
////        UserReviewPostRequest request = new UserReviewPostRequest();
////        request.setAnimeID(50);
////        request.setUserID("userId");
////        request.setRating(90);
////        request.setReview("Great anime");
////
////        ArgumentCaptor<ReviewRecord> reviewCaptor = ArgumentCaptor.forClass(ReviewRecord.class);
////
////        Review review = service.postReview(request);
////
////        Assertions.assertNotNull(review);
////
////        verify(repository).save(reviewCaptor.capture());
////
////        ReviewRecord capturedReview = reviewCaptor.getValue();
////
////        Assertions.assertNotNull(capturedReview, "The review record is returned");
////        assertEquals(capturedReview.getAnimeID(), request.getAnimeID());
////        assertEquals(capturedReview.getUserID(), request.getUserID());
////        assertEquals(capturedReview.getRating(), request.getRating());
////        assertEquals(capturedReview.getReview(), request.getReview());
////    }
////
////    @Test
////    void findReview_existingReview_recordFound() {
////        // Arrange
////        String reviewId = "existing-review-id";
////        ReviewRecord reviewRecord = new ReviewRecord();
////        reviewRecord.setReviewID(reviewId);
////        reviewRecord.setReview("Review Title");
////        // Mock the behavior of the repository
////        when(repository.findById(eq(reviewId))).thenReturn(Optional.of(reviewRecord));
////
////        // Act
////        Review result = service.findReview(reviewId);
////
////        // Assert
////        Assertions.assertNotNull(result, "The review should not be null");
////        Assertions.assertEquals(reviewRecord.getReviewID(), result.getReviewID(), "The review ID should match");
////        Assertions.assertEquals(reviewRecord.getReview(), result.getReview(), "The review title should match");
////
////        // Verify that the repository method was called with the correct argument
////        verify(repository).findById(eq(reviewId));
////    }
////
////    @Test
////    void findReview_nonexistentReview_recordNotFound() {
////        // Arrange
////        String reviewId = "nonexistent-review-id";
////        // Mock the behavior of the repository
////        when(repository.findById(eq(reviewId))).thenReturn(Optional.empty());
////
////        // Act
////        Review result = service.findReview(reviewId);
////
////        // Assert
////        Assertions.assertNull(result, "The review should be null");
////
////        // Verify that the repository method was called with the correct argument
////        verify(repository).findById(eq(reviewId));
////    }
////
////
////    @Test
////    void deleteReview_shouldCallRepositoryDeleteById() {
////        String reviewId = "exampleReviewId";
////
////        service.deleteReview(reviewId);
////
////        verify(repository).deleteById(reviewId);
////    }
////
////    @Test
////    void getReviewsForAnime_firstPage_shouldQueryPageWithCorrectValues() {
////        // Arrange
////        int animeId = 50;
////
////        Map<String, AttributeValue> valueMap = new HashMap<>();
////        valueMap.put(":animeID", new AttributeValue().withN(String.valueOf(animeId)));
////        valueMap.put(":postDate", new AttributeValue().withN(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)));
////
////        DynamoDBQueryExpression<ReviewRecord> queryExpression = new DynamoDBQueryExpression<ReviewRecord>()
////                .withIndexName(ReviewRecord.REVIEW_lOOK_UP)
////                .withConsistentRead(false)
////                .withLimit(10)
////                .withKeyConditionExpression("animeID = :animeID and postDate <= :postDate")
////                .withExpressionAttributeValues(valueMap);
////
////        QueryResultPage<ReviewRecord> expectedPage = new QueryResultPage<>();
////        when(mapper.queryPage(eq(ReviewRecord.class), eq(queryExpression))).thenReturn(expectedPage);
////
////        // Act
////        QueryResultPage<ReviewRecord> result = service.getReviewsForAnime(animeId);
////
////        // Assert
////        assertNotNull(result);
////        assertEquals(expectedPage, result);
////
////        // Verify that the mapper's queryPage method was called with the correct arguments
////        verify(mapper).queryPage(eq(ReviewRecord.class), eq(queryExpression));
////    }
////
////    @Test
////    void getReviewsForAnime_withLastKey_shouldQueryPageWithExclusiveStartKey() {
////        // Arrange
////        int animeId = 50;
////        Map<String, AttributeValue> lastKey = new HashMap<>();
////        lastKey.put("postDate", new AttributeValue().withN("20230708"));
////
////        Map<String, AttributeValue> valueMap = new HashMap<>();
////        valueMap.put(":animeID", new AttributeValue().withN(String.valueOf(animeId)));
////        valueMap.put(":postDate", new AttributeValue().withN(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)));
////
////        DynamoDBQueryExpression<ReviewRecord> queryExpression = new DynamoDBQueryExpression<ReviewRecord>()
////                .withIndexName(ReviewRecord.REVIEW_lOOK_UP)
////                .withConsistentRead(false)
////                .withLimit(10)
////                .withExclusiveStartKey(lastKey)
////                .withKeyConditionExpression("animeID = :animeID and postDate <= :postDate")
////                .withExpressionAttributeValues(valueMap);
////
////        QueryResultPage<ReviewRecord> expectedPage = new QueryResultPage<>();
////        when(mapper.queryPage(eq(ReviewRecord.class), eq(queryExpression))).thenReturn(expectedPage);
////
////        // Act
////        QueryResultPage<ReviewRecord> result = service.getReviewsForAnime(animeId, lastKey);
////
////        // Assert
////        assertNotNull(result);
////        assertEquals(expectedPage, result);
////
////        // Verify that the mapper's queryPage method was called with the correct arguments
////        verify(mapper).queryPage(eq(ReviewRecord.class), eq(queryExpression));
////    }
//
////    @Test
////    void calculateAverageRatingByAnime_noRecordsFound_shouldReturnNull() {
////        // Arrange
////        int animeId = 50;
////
////        Map<String, AttributeValue> valueMap = new HashMap<>();
////        valueMap.put(":animeID", new AttributeValue().withN(String.valueOf(animeId)));
////        valueMap.put(":postDate", new AttributeValue().withN(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)));
////
////        DynamoDBQueryExpression<ReviewRecord> queryExpression = new DynamoDBQueryExpression<ReviewRecord>()
////                .withIndexName(ReviewRecord.REVIEW_lOOK_UP)
////                .withConsistentRead(false)
////                .withKeyConditionExpression("animeID = :animeID and postDate <= :postDate")
////                .withExpressionAttributeValues(valueMap);
////
////        PaginatedQueryList<ReviewRecord> emptyList = new PaginatedQueryList<>();
////        when(mapper.query(eq(ReviewRecord.class), eq(queryExpression))).thenReturn(emptyList);
////
////        // Act
////        Integer result = service.calculateAverageRatingByAnime(animeId);
////
////        // Assert
////        assertNull(result);
////
////        // Verify that the mapper's query method was called with the correct arguments
////        verify(mapper).query(eq(ReviewRecord.class), eq(queryExpression));
////    }
////
////    @Test
////    void calculateAverageRatingByAnime_recordsFound_shouldReturnAverageRating() {
////        // Arrange
////        int animeId = 50;
////
////        Map<String, AttributeValue> valueMap = new HashMap<>();
////        valueMap.put(":animeID", new AttributeValue().withN(String.valueOf(animeId)));
////        valueMap.put(":postDate", new AttributeValue().withN(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)));
////
////        DynamoDBQueryExpression<ReviewRecord> queryExpression = new DynamoDBQueryExpression<ReviewRecord>()
////                .withIndexName(ReviewRecord.REVIEW_lOOK_UP)
////                .withConsistentRead(false)
////                .withKeyConditionExpression("animeID = :animeID and postDate <= :postDate")
////                .withExpressionAttributeValues(valueMap);
////
////        ReviewRecord record1 = new ReviewRecord();
////        record1.setRating(80);
////
////        ReviewRecord record2 = new ReviewRecord();
////        record2.setRating(90);
////
////        PaginatedQueryList<ReviewRecord> reviewRecords = new PaginatedQueryList<>();
////        reviewRecords.add(record1);
////        reviewRecords.add(record2);
////
////        when(mapper.query(eq(ReviewRecord.class), eq(queryExpression))).thenReturn(reviewRecords);
////
////        // Act
////        Integer result = service.calculateAverageRatingByAnime(animeId);
////
////        // Assert
////        assertNotNull(result);
////        assertEquals(85, result);
////
////        // Verify that the mapper's query method was called with the correct arguments
////        verify(mapper).query(eq(ReviewRecord.class), eq(queryExpression));
////    }
//}
////    @Test
////    public void post_validInputs_returnCreatedReview(){
////        UserReviewPostRequest request = new UserReviewPostRequest();
////        request.setReview("testReview");
////        request.setRating(100);
////        request.setDisplayName("testUser");
////        request.setAnimeID(1);
////
////        Review review = reviewService.postReview(request);
////
////        Assertions.assertEquals(100, review.getRating());
////        Assertions.assertEquals("testReview", review.getReview());
////        Assertions.assertEquals(1, review.getAnimeID());
////        Assertions.assertEquals("testUser", review.getDisplayName());
////
////    }
////
////    @Test
////    public void delete_anyInput_callsRepository(){
////        String uuid = "testUser";
////
////        reviewService.deleteReview(uuid);
////
////        verify(reviewRepository, times(1)).deleteById(uuid);
////    }
////
////    @Test
////    public void findReview_validID_returnsReview(){
////        UserReviewPostRequest request = new UserReviewPostRequest();
////        request.setReview("testReview");
////        request.setRating(100);
////        request.setDisplayName("testUser");
////        request.setAnimeID(1);
////
////        Review createdReview = reviewService.postReview(request);
////
////        ReviewRecord reviewRecord = new ReviewRecord();
////        reviewRecord.setReviewID(createdReview.getReviewID());
////        reviewRecord.setRating(createdReview.getRating());
////        reviewRecord.setUserID(createdReview.getDisplayName());
////        reviewRecord.setAnimeID(createdReview.getAnimeID());
////        reviewRecord.setReview(createdReview.getReview());
////        reviewRecord.setPostDate(createdReview.getPostDate());
////
////        when(reviewRepository.findById(createdReview.getReviewID())).thenReturn(Optional.of(reviewRecord));
////        Review foundReview = reviewService.findReview(createdReview.getReviewID());
////
////        Assertions.assertEquals(createdReview.getReviewID(), foundReview.getReviewID());
////
////    }
////
////    @Test
////    public void findReview_invalidID_returnsReview() {
////        String reviewID = "id does not exist";
////
////        when(reviewRepository.findById(reviewID)).thenReturn(Optional.ofNullable(null));
////        Review notFoundReview = reviewService.findReview(reviewID);
////
////        Assertions.assertNull(notFoundReview);
////    }
////
////    @Test
////    public void getReviewsForAnime_validAnime_ReturnsPageOfReviews(){
////        UserReviewPostRequest request = new UserReviewPostRequest();
////        request.setReview("testReview");
////        request.setRating(100);
////        request.setDisplayName("testUser");
////        request.setAnimeID(1);
////
////        UserReviewPostRequest request2 = new UserReviewPostRequest();
////        request.setReview("testReview2");
////        request.setRating(94);
////        request.setDisplayName("testUser2");
////        request.setAnimeID(1);
////
////        Review createdReview = reviewService.postReview(request);
////        Review createdReview2 = reviewService.postReview(request2);
////
////        ReviewRecord reviewRecord = new ReviewRecord();
////        reviewRecord.setPostDate(createdReview.getPostDate());
////        reviewRecord.setUserID(createdReview.getDisplayName());
////        reviewRecord.setAnimeID(createdReview.getAnimeID());
////        reviewRecord.setRating(createdReview.getRating());
////        reviewRecord.setReviewID(createdReview.getReviewID());
////        reviewRecord.setReview(createdReview.getReview());
////
////        ReviewRecord reviewRecord2 = new ReviewRecord();
////        reviewRecord.setPostDate(createdReview2.getPostDate());
////        reviewRecord.setUserID(createdReview2.getDisplayName());
////        reviewRecord.setAnimeID(createdReview2.getAnimeID());
////        reviewRecord.setRating(createdReview2.getRating());
////        reviewRecord.setReviewID(createdReview2.getReviewID());
////        reviewRecord.setReview(createdReview2.getReview());
////
////        List<ReviewRecord> reviewRecordList = new ArrayList<>(Arrays.asList(reviewRecord, reviewRecord2));
////
////
////        when(mapper.queryPage(eq(ReviewRecord.class),
////                any(DynamoDBQueryExpression.class)))
////                .thenReturn(mock(QueryResultPage.class,
////                        withSettings().defaultAnswer(new ForwardsInvocations(reviewRecordList))));
////
////        QueryResultPage<ReviewRecord> resultPage = reviewService.getReviewsForAnime(1);
////        Assertions.assertNotNull(resultPage);
////    }
////
//
