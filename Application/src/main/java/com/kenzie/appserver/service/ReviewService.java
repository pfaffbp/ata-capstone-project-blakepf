package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
import com.kenzie.appserver.repositories.ReviewRepository;
import com.kenzie.appserver.repositories.model.ReviewRecord;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.Review;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.kenzie.appserver.repositories.model.ReviewRecord.REVIEW_lOOK_UP;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;
    private final DynamoDBMapper mapper;

    public ReviewService(ReviewRepository reviewRepository, DynamoDBMapper mapper){
        this.reviewRepository = reviewRepository;
        this.mapper = mapper;
    }

    public Review postReview(UserReviewPostRequest userReviewPostRequest){
        Review review = new Review(
                userReviewPostRequest.getAnimeID(),
                userReviewPostRequest.getUserID(),
                userReviewPostRequest.getRating(),
                userReviewPostRequest.getReview());

        reviewRepository.save(reviewIntoRecord(review));

        return review;
    }

    public Review findReview(String uuid){
        Optional<ReviewRecord> reviewRecord = reviewRepository.findById(uuid);
        return reviewRecord.map(this::recordIntoRecord).orElse(null);
    }

    public void deleteReview(String uuid){
        reviewRepository.deleteById(uuid);
    }


    public PaginatedQueryList<ReviewRecord> getReviewsForAnime(int id){
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":animeID", new AttributeValue().withN(String.valueOf(id)));
        valueMap.put(":postDate", new AttributeValue().withN(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)));

        DynamoDBQueryExpression<ReviewRecord> queryExpression = new DynamoDBQueryExpression<ReviewRecord>()
                .withIndexName(REVIEW_lOOK_UP)
                .withConsistentRead(false)
                .withKeyConditionExpression("animeID = :animeID and postDate <= :postDate")
                .withExpressionAttributeValues(valueMap);

        PaginatedQueryList<ReviewRecord> reviewRecords = mapper.query(ReviewRecord.class, queryExpression);
        return reviewRecords;
    }

//    public Review updateReview()  //need some other things for this.

    private Review recordIntoRecord(ReviewRecord reviewRecord){
        Review review = new Review();
        review.setRating(reviewRecord.getRating());
        review.setReviewID(reviewRecord.getReviewID());
        review.setReview(reviewRecord.getReview());
        review.setUserID(reviewRecord.getUserID());
        review.setPostDate(reviewRecord.getPostDate());
        review.setAnimeID(reviewRecord.getAnimeID());

        return review;
    }
    private ReviewRecord reviewIntoRecord(Review review){
        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setReviewID(review.getReviewID());
        reviewRecord.setReview(review.getReview());
        reviewRecord.setRating(review.getRating());
        reviewRecord.setAnimeID(review.getAnimeID());
        reviewRecord.setPostDate(review.getPostDate());
        reviewRecord.setUserID(review.getUserID());

        return reviewRecord;
    }
}
