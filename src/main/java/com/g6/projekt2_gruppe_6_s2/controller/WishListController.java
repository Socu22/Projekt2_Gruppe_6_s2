package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepository;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String WishList(@RequestParam("id") int id, HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
        }
        boolean isUser = false;
        if (user!=null && repo.userOwnsList(user.getId(), id)){
           isUser = true;

        }
        System.out.println(isUser);
        model.addAttribute("isUser",isUser );

        var wishList = new WishList();
        wishList.setWishes(repo.getWishList(id ));

        model.addAttribute("wishList",wishList);
        return "wishList";
    }
}
