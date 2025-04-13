package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.repository.UserRepositoryDatabase;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController //for pages independent on repos, sessions, and "advanced" handling
{

    @GetMapping("/")// This is the homepage
    public String getIndex(){
        return "index";
    }

    @GetMapping("/Home")// This is the homepage
    public String home(HttpServletRequest request, Model model){
        UserController.getSession(request, model);
        return "index";
    }

    @GetMapping("/AboutUs")// This is the homepage
    public String aboutUs(HttpServletRequest request, Model model){
        UserController.getSession(request, model);
        return "aboutUs";
    }

    @GetMapping("/error")// This is in case an html error happens
    public String error(){
        return "404";
    }

}
