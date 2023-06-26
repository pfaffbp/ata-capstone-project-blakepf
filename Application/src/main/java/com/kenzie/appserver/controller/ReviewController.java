package com.kenzie.appserver.controller;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.kenzie.appserver.controller.reviewModels.GetReviewsRequest;
import com.kenzie.appserver.controller.reviewModels.ReviewResponse;
import com.kenzie.appserver.controller.reviewModels.UserReviewPostRequest;
import com.kenzie.appserver.repositories.model.ReviewRecord;
import com.kenzie.appserver.service.ReviewService;
import com.kenzie.appserver.service.model.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/{animeID}")
    public ResponseEntity<List<ReviewResponse>> reviewsFromAnimeID(@PathVariable int animeID){
        PaginatedQueryList<ReviewRecord> reviewRecords = reviewService.getReviewsForAnime(animeID);


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
        reviewResponse.setUserID(review.getUserID());
        reviewResponse.setPostDate(review.getPostDate());

        return reviewResponse;
    }

    private ReviewResponse reviewRecordToResponse(ReviewRecord review){
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviewID(review.getReviewID());
        reviewResponse.setReview(review.getReview());
        reviewResponse.setRating(review.getRating());
        reviewResponse.setAnimeID(review.getAnimeID());
        reviewResponse.setUserID(review.getUserID());
        reviewResponse.setPostDate(review.getPostDate());

        return reviewResponse;
    }
}
