package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.repository.UserRepositoryDatabase;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
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
    public String home(){
        return "index";
    }
    @GetMapping("/AboutUs")// This is the homepage
    public String aboutUs(){
        return "aboutUs";
    }

}
