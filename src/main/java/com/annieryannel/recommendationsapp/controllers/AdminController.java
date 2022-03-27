package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String home_base(Model model) {
        model.addAttribute("users", userService.loadAllUsers());
        return "admin";
    }

}
