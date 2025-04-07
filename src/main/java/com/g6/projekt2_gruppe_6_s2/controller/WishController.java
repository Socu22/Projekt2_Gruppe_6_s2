package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepository;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Locale;

@Controller
public class WishController {
    
    @Autowired
    WishRepositoryDataBase repo;
    WishRepository repo2;

    @GetMapping("/Profile")// change this to connect to a users list somehow
    public String getIndex(Model model){
        var wishLists = new ArrayList<WishList>();
        wishLists.addAll(repo.getWishLists(1));
        for (int i = 0; i < wishLists.size(); i++) {
            wishLists.get(i).setWishes(repo.getWishList(wishLists.get(i).getListId()));

            ArrayList<Wish> list = wishLists.get(i).getWishes();
            while (list.size() > 3) {
                list.remove(list.size() - 1);
            }
            wishLists.get(i).setWishes(list);
        }
        model.addAttribute("wishLists",wishLists);
        return "Profile";
    }

}
