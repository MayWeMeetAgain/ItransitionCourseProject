package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.DTO.CardDTO;
import com.annieryannel.recommendationsapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewsController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/reviews/getAll")
    public List<CardDTO> getAllCards() {
        return reviewService.loadAllCards();
    }


    @PostMapping("/reviews/{reviewId}/like")
    public Integer likeReview(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        return reviewService.likeReview(reviewId, authentication.getName());
    }

    @DeleteMapping("/reviews/{reviewId}/like")
    public Integer unlikeReview(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        return reviewService.unlikeReview(reviewId, authentication.getName());
    }


}
