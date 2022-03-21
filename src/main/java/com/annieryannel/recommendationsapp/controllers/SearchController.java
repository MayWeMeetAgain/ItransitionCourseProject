package com.annieryannel.recommendationsapp.controllers;

import java.util.List;

import com.annieryannel.recommendationsapp.DTO.CardDTO;
import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.repositories.ReviewRepository;
import com.annieryannel.recommendationsapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class SearchController {

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = "/search")
    public String search(@RequestParam(value = "search", required = false) String text, Model model) {
        List<CardDTO> searchResults = null;
        try {
            searchResults = reviewService.search(text);

        } catch (Exception ex) {
            // Nothing
        }
        model.addAttribute("search", searchResults);
        return "searchresults";

    }

}
