package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.repository.UserRepository;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class PageController {


    @Autowired
    WishRepositoryDataBase repo;

    @Autowired
    UserRepository repoUser;


    // Login form
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = repoUser.getUserWithUsernamePassword(username, password);
        if (user != null) {
            model.addAttribute("currentUser", user);
            return "redirect:/dashboard"; // Redirect to a dashboard or home page after successful login
        } else {
            model.addAttribute("loginError", true);
            return "login"; // Return to the login page with an error message
        }
    }


    // Login form with error
    @GetMapping("/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
