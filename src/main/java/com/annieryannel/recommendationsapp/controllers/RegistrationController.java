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

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String showRegistration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        if (!isPasswordMatch(model, userForm) || doesUserExist(model, userForm)) {
            return "registration";
        }
        model.addAttribute("registered", "You have been successfully registered");
        return "login";
    }


    private boolean isPasswordMatch(Model model, User userForm) {
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Passwords don't match");
            return false;
        }
        return true;
    }

    private boolean doesUserExist(Model model, User userForm) {
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "User with this username already exist");
            return true;
        }
        return false;
    }
}