package com.kenzie.appserver.controller;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kenzie.appserver.controller.reviewModels.AttributeValuesForReviews;
import com.kenzie.appserver.controller.reviewModels.GetReviewsRequest;
import com.kenzie.appserver.controller.reviewModels.ReviewResponse;
import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
import com.kenzie.appserver.repositories.model.ReviewRecord;
import com.kenzie.appserver.service.ReviewService;
import com.kenzie.appserver.service.model.Review;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/review")
public class ReviewController {


    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping()
    public ResponseEntity<ReviewResponse> postReview(@RequestBody UserReviewPostRequest userReviewPostRequest){
        Review review = reviewService.postReview(userReviewPostRequest);

        return ResponseEntity.ok(reviewToResponse(review));
    }

    @DeleteMapping("/{reviewID}")
    public ResponseEntity deleteReviewByID(@PathVariable("reviewID") String reviewID){
        reviewService.deleteReview(reviewID);
        return ResponseEntity.status(204).build();
    }

//    @GetMapping("/{animeID}")
//    public ResponseEntity<List<ReviewResponse>> reviewsFromAnimeID(@PathVariable int animeID){
//        PaginatedQueryList<ReviewRecord> reviewRecords = reviewService.getReviewsForAnime(animeID);
//
//
//        List<ReviewResponse> reviewResponseList = reviewRecords.stream()
//                .map(this::reviewRecordToResponse)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(reviewResponseList);
//    }

    @PostMapping("/limit")
    public ResponseEntity<HashMap<Integer, Object>> reviewsFromAnimeID(@RequestBody GetReviewsRequest reviewsRequest){
        QueryResultPage<ReviewRecord> reviewRecords
                = reviewService.getReviewsForAnime(reviewsRequest.getAnimeID(),
                convertValuesIntoExclusiveKey(reviewsRequest.getValuesForReviews()));

        AttributeValuesForReviews attributeValuesForReviews = extractValuesFromLastKey(reviewRecords.getLastEvaluatedKey());

        List<ReviewResponse> reviewResponseList = reviewRecords.getResults()
                .stream()
                .map(this::reviewRecordToResponse)
                .collect(Collectors.toList());

        HashMap<Integer, Object> hashMap = new HashMap<>();
        hashMap.put(1, reviewResponseList);
        hashMap.put(2, attributeValuesForReviews);

        return ResponseEntity.ok(hashMap);
    }

    @GetMapping("/{animeID}")
    public ResponseEntity<Integer> calculateAverageScore(@PathVariable int animeID){
        Integer averageScore = reviewService.calculateAverageRatingByAnime(animeID);
        return ResponseEntity.ok(Objects.requireNonNullElse(averageScore, 0));
        // if non null return average score, if null return 0
    }

    @GetMapping("username/{displayName}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByDisplayName(@PathVariable String displayName){
        PaginatedQueryList<ReviewRecord> reviewRecords = reviewService.getListOfAnimeByDisplayName(displayName);

        List<ReviewResponse> reviewResponseList = reviewRecords.stream()
                .map(this::reviewRecordToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewResponseList);
    }

    private ReviewResponse reviewToResponse(Review review){
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviewID(review.getReviewID());
        reviewResponse.setReview(review.getReview());
        reviewResponse.setRating(review.getRating());
        reviewResponse.setAnimeID(review.getAnimeID());
        reviewResponse.setUserID(review.getDisplayName());
        reviewResponse.setPostDate(review.getPostDate());

        return reviewResponse;
    }

    private ReviewResponse reviewRecordToResponse(ReviewRecord review){
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviewID(review.getReviewID());
        reviewResponse.setReview(review.getReview());
        reviewResponse.setRating(review.getRating());
        reviewResponse.setAnimeID(review.getAnimeID());
        reviewResponse.setUserID(review.getDisplayName());
        reviewResponse.setPostDate(review.getPostDate());

        return reviewResponse;
    }

    private AttributeValuesForReviews extractValuesFromLastKey(Map<String, AttributeValue> attributeMap){
        if(ifNull(attributeMap)){
            return null;
        }
        String animeID = "animeID";
        String postDate = "postDate";
        AttributeValuesForReviews attributeValuesForReviews = new AttributeValuesForReviews();

        for(Map.Entry<String, AttributeValue> valueMap : attributeMap.entrySet()){
            if(valueMap.getKey().equals(animeID)){
                attributeValuesForReviews.setAnimeID(Integer.parseInt(valueMap.getValue().getN()));
            }else if(valueMap.getKey().equals(postDate)){
                attributeValuesForReviews.setPostDate(Integer.parseInt(valueMap.getValue().getN()));
            }else{
                attributeValuesForReviews.setReviewID(valueMap.getValue().getS());
            }
        }
        return attributeValuesForReviews;
    }

    private Map<String, AttributeValue> convertValuesIntoExclusiveKey(AttributeValuesForReviews attributes){
        if(ifNull(attributes)){
            return null;
        }
        Map<String, AttributeValue> exclusiveKey = new HashMap<>();
        exclusiveKey.put("animeID", new AttributeValue().withN(String.valueOf(attributes.getAnimeID())));
        exclusiveKey.put("postDate", new AttributeValue().withN(String.valueOf(attributes.getPostDate())));
        exclusiveKey.put("reviewID", new AttributeValue(attributes.getReviewID()));

        return exclusiveKey;
    }
//    @PutMapping("/{displayName}")
//    private ResponseEntity<Integer> addLikes(@PathVariable String displayName){
//
//    }
    private boolean ifNull(Object obj){
        return obj == null;
    }
}
