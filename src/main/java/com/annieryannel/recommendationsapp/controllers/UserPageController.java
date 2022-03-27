package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.DTO.UserDto;
import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.models.User;
import com.annieryannel.recommendationsapp.service.ReviewService;
import com.annieryannel.recommendationsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/user/{username}")
    public String showProfile(@PathVariable("username") String username, Model model) {
        UserDto userDto = userService.getUserDtoByUsername(username);
        model.addAttribute("user", userDto);
        model.addAttribute("cards", reviewService.getReviewsByUsername(username));
        return "userpage";
    }
}
