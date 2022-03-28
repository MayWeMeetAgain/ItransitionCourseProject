package com.annieryannel.recommendationsapp.controllers;

import com.annieryannel.recommendationsapp.models.User;
import com.annieryannel.recommendationsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
        boolean isPasswordMatch = userForm.getPassword().equals(userForm.getPasswordConfirm());
        boolean isUserExist = !userService.saveUser(userForm);
        model.addAttribute("passwordError" , !isPasswordMatch);
        model.addAttribute("usernameError", isUserExist);
        if (!isPasswordMatch || isUserExist || bindingResult.hasErrors()) {
            return "registration";
        }
        model.addAttribute("registered", true);
        return "login";
    }
}