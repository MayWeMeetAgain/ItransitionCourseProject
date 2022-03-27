package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.DTO.ReviewDto;
import com.annieryannel.recommendationsapp.service.FirebaseStorageStrategy;
import com.annieryannel.recommendationsapp.service.ReviewService;
import com.annieryannel.recommendationsapp.service.StorageStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    FirebaseStorageStrategy storageStrategy;

    @GetMapping("/reviews/{reviewId}")
    public String showReview(@PathVariable("reviewId") Long reviewId, Model model) {
        model.addAttribute("review", reviewService.loadById(reviewId));
        return "review";
    }

    @GetMapping("/addReview")
    public String addReview() {
        return "addreview";
    }

    @PostMapping("/addReview")
    public String addCard(ReviewDto dto, @RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        storageStrategy.uploadFile(file);

        reviewService.saveReview(dto, authentication.getName());
        return "addreview";
    }

}
