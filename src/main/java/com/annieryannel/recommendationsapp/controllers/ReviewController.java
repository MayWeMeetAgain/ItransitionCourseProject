package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.DTO.ReviewDto;
import com.annieryannel.recommendationsapp.service.ReviewService;
import com.annieryannel.recommendationsapp.utils.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class ReviewController {

    final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping("/reviews/{reviewId}")
    public String readReview(@PathVariable("reviewId") Long reviewId, Model model) {
            model.addAttribute("review", reviewService.readById(reviewId));

        return "review";
    }

    @GetMapping("/review/edit/{reviewId}")
    public String editReview(@PathVariable("reviewId") Long reviewId, Model model, Authentication authentication) {
        model.addAttribute("review", reviewService.loadById(reviewId, authentication));
        model.addAttribute("categories", Category.values());
        return "addreview";
    }

    @PostMapping("/review/edit/{reviewId}")
    public String saveEditedReview(@PathVariable("reviewId") Long reviewId, @ModelAttribute("review") @Valid ReviewDto dto, BindingResult bindingResult, Authentication authentication) {
        dto.setId(reviewId);
        reviewService.saveReview(dto, authentication);
        return "redirect:/";
    }

    @GetMapping("/review/add")
    public String addReview(Model model) {
        model.addAttribute("review", new ReviewDto());
        model.addAttribute("categories", Category.values());
        return "addreview";
    }

    @PostMapping("/review/add")
    public String saveAddedReview(@ModelAttribute("review") @Valid ReviewDto dto, BindingResult bindingResult, Authentication authentication) {
        reviewService.addReview(dto, authentication.getName(), authentication);
        return "addreview";
    }

}
