package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepository;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class WishListController {
    @Autowired
    WishRepositoryDataBase repo;

    @GetMapping("/WishList")// change this to connect to a users list somehow
    public String getIndex(@RequestParam() int id, Model model){

        var wishList = new WishList();
        wishList.setWishes(repo.getWishList(1));

        model.addAttribute("wishList",wishList);
        return "wishList";
    }
}
