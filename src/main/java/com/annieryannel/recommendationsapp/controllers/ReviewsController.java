package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.DTO.ReviewDto;
import com.annieryannel.recommendationsapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@RestController
public class ReviewsController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/reviews/getAll")
    public List<ReviewDto> getAllCards() {
        return reviewService.loadAll();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reviews/{reviewId}/rate")
    public Float rateReview(@RequestParam Integer rate, @PathVariable("reviewId") Long reviewId , Authentication authentication) {
        return reviewService.rateReview(rate, reviewId, authentication.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reviews/{reviewId}/like")
    public Integer likeReview(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
       return reviewService.likeReview(reviewId, authentication.getName());
    }

    @DeleteMapping("/reviews/{reviewId}/like")
    public Integer unlikeReview(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        return reviewService.unlikeReview(reviewId, authentication.getName());
    }

    @DeleteMapping("/review/delete/{reviewId}")
    public void showReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReviewById(reviewId);
    }
}
