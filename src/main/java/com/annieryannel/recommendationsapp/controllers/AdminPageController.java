package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    private final UserService userService;

    @Autowired
    public AdminPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("users", userService.loadAllUsers());
        return "admin";
    }

}
