package com.gomez.docutrack.bundle.controller;

import com.gomez.docutrack.bundle.entities.User;
import com.gomez.docutrack.bundle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        
        model.addAttribute("user", new User());
        model.addAttribute("sections", User.SectionName.values());
        
        model.addAttribute("genders", User.Gender.values());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.registerUser(user);
        return "redirect:/login?success=true";
    }
}
