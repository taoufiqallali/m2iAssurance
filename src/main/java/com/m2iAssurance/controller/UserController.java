package com.m2iAssurance.controller;


import com.m2iAssurance.model.User;
import com.m2iAssurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users"; // View: users.html
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // View: register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.saveUser(user);
            return "redirect:/login"; // Redirect to login if successful
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // Return to the registration form with the error
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }


    @GetMapping("/personalInfo")
    public String personlaInfo(Model model, Principal principal){

        String username = principal.getName();

        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        return "personalInfo";
    }

    @PostMapping("/personalInfo/update/{username}")
    public String updateUser(@PathVariable("username") String username,
                             User user,
                             Model model) {

        user = userService.updateUser(username, user);
        model.addAttribute("user", user);

        return "redirect:/users/personalInfo";

    }


}
