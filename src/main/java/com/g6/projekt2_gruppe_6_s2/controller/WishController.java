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
    //WishRepository repo;

    @GetMapping("/")
    public String getIndex(Model model){
        var wishList = new ArrayList<>();
        wishList.addAll(repo.getWishList());
        model.addAttribute("wishList",wishList);
        return "index";
    }
}
