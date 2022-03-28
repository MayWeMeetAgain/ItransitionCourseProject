package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.DTO.ReviewDto;
import com.annieryannel.recommendationsapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ReviewEvaluationController {

    final ReviewService reviewService;

    @Autowired
    public ReviewEvaluationController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review/rate/{reviewId}")
    public Float rateReview(@RequestParam Integer rate, @PathVariable("reviewId") Long reviewId , Authentication authentication) {
        return reviewService.rateReview(rate, reviewId, authentication.getName());
    }

    @PostMapping("/review/like/{reviewId}")
    public Integer likeReview(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
       return reviewService.likeReview(reviewId, authentication.getName());
    }

    @DeleteMapping("/review/like/{reviewId}")
    public Integer unlikeReview(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        return reviewService.unlikeReview(reviewId, authentication.getName());
    }

    @DeleteMapping("/review/delete/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        reviewService.deleteReviewById(reviewId, authentication);
    }

}
