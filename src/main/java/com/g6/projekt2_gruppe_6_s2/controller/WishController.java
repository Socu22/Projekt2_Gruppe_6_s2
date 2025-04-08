package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepository;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Locale;

@Controller
public class WishController {
    
    @Autowired
    WishRepositoryDataBase repo;
    WishRepository repo2;

    @GetMapping("/Profile")// change this to connect to a users list somehow
    public String getIndex(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("activeUser");


        var wishLists = new ArrayList<WishList>();
        if(user!=null){
            System.out.println("user exists");
            wishLists.addAll(repo.getWishLists(user.getId()));
            for (int i = 0; i < wishLists.size(); i++) {
                wishLists.get(i).setWishes(repo.getWishList(wishLists.get(i).getListId()));

                ArrayList<Wish> list = wishLists.get(i).getWishes();
                while (list.size() > 3) {
                    list.remove(list.size() - 1);
                }
                wishLists.get(i).setWishes(list);
            }
            model.addAttribute("wishLists",wishLists);
        }


        return "profile";
    }


    @GetMapping("/saveCreateWishList")
    public String postCreateWishlist(@RequestParam("listId") int listId,
                                     @RequestParam("wishId") int wishId)
    {
        WishList wishList =null;

        //repo.save(wishList);
        return "createWishList";
    }

}
