package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public String userAction(Model model) {
        model.addAttribute("cards", reviewService.loadAll());
        //model.addAttribute("reviews", reviewService.loadAllReviews());
        return "home";
    }
}
