package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;


@Controller
public class ShowWishController {
    @Autowired
    WishRepositoryDataBase repo;

    @GetMapping("/Wish")// change this to connect to a users list somehow
    public String Wish(@RequestParam("id") int id, Model model){
        Wish wish;
        wish = repo.getWish(id);
        model.addAttribute("wish",wish);
        return "showWish";
    }
}
