package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.repository.WishRepository;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class WishController {
    
    @Autowired
    WishRepositoryDataBase repo;
    WishRepository repo2;

    @GetMapping("/")// change this to connect to a users list somehow
    public String getIndex(Model model){
        var wishLists = new ArrayList<>();
        wishLists.addAll(repo.getWishLists(1));
        model.addAttribute("wishLists",wishLists);
        return "Profile";
    }

}
