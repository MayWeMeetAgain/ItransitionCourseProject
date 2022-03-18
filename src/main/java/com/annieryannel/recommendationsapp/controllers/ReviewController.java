package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.DTO.CardDTO;
import com.annieryannel.recommendationsapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/reviews/{reviewId}")
    public String showReview(@PathVariable("reviewId") Long reviewId, Model model) {
        model.addAttribute("review", reviewService.loadReviewById(reviewId));
        return "review";
    }

    @GetMapping("/addReview")
    public String addReview() {
        return "addreview";
    }
}
