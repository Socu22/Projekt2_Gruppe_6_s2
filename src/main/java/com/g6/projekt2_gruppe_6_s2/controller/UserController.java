package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.repository.UserRepositoryDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    UserRepositoryDatabase repoUser;






    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {
        User user = repoUser.getUserWithUsernamePassword(username, password);
        if (user != null) {
            model.addAttribute("currentUser", user);
            System.out.println("Works");
            return "redirect:/"; // Redirect to a dashboard or home page after successful login
        } else {
            model.addAttribute("loginError", true);
            System.out.println("Fails");
            return "login"; // Return to the login page with an error message
        }
    }










}
