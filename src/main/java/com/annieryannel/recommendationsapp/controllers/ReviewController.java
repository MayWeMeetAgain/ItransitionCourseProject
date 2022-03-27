package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.DTO.ReviewDto;
import com.annieryannel.recommendationsapp.service.ReviewService;
import com.annieryannel.recommendationsapp.utils.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;

//    @Autowired
//    FirebaseStorageStrategy storageStrategy;

    @GetMapping("/reviews/{reviewId}")
    public String readReview(@PathVariable("reviewId") Long reviewId, Model model) {
        model.addAttribute("review", reviewService.readById(reviewId));
        return "review";
    }

    @GetMapping("/review/edit/{reviewId}")
    public String editReview(@PathVariable("reviewId") Long reviewId, Model model) {
        model.addAttribute("review", reviewService.loadById(reviewId));
        model.addAttribute("categories", Category.values());
        return "addreview";
    }

    @PostMapping("/review/edit/{reviewId}")
    public String saveEditedReview(@PathVariable("reviewId") Long reviewId, @ModelAttribute("review") ReviewDto dto, @RequestParam("file") File file, Authentication authentication) {
        dto.setId(reviewId);
        reviewService.saveReview(dto);
        return "redirect:/";
    }

    @GetMapping("/addReview")
    public String addReview(Model model) {
        model.addAttribute("review", new ReviewDto());
        model.addAttribute("categories", Category.values());
        return "addreview";
    }

    @PostMapping("/addReview")
    public String addCard(@ModelAttribute("review") ReviewDto dto, @RequestParam("file") File file, Authentication authentication) throws IOException {
//        storageStrategy.uploadFile(file);

        reviewService.addReview(dto, authentication.getName());
        return "redirect:/";
    }

}
