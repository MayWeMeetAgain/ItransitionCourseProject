package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Controller
public class HomePageController {

    private final ReviewService reviewService;

    @Autowired
    public HomePageController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("cards", reviewService.loadAll());
        return "home";
    }
}
