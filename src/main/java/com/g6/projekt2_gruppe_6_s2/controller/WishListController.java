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

import java.sql.SQLException;
import java.util.ArrayList;

@Controller
public class WishListController {
    @Autowired
    WishRepositoryDataBase repo;

    @GetMapping("/Profile")// shows wishLists Based on current activeUser
    public String getIndex(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");

        }
        model.addAttribute("isLoggedIn",user != null);


        var wishLists = new ArrayList<WishList>();
        if(user!=null){
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



    @GetMapping("/getCreateWishList")
    public String getCreateWishList() {
        return "createWishList";
    }


    @GetMapping("/saveCreateWishList")
    public String postCreateWishlist(HttpServletRequest request, @RequestParam("title") String title ) throws SQLException {

        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
        }

        int listId = repo.createWishlist(user.getId(),title); //makes all necessary logic for creating something in this database. works with my lucas.sql

//        ArrayList<Wish> wishList = new ArrayList<>();
//        wishList.add(new Wish(repo.getNextWishId(),"t2"));
//        WishList wishListInstance = new WishList(title,listId,wishList);
//        repo.saveWishlist(1,listId,wishListInstance); //saves a dummy
        return "redirect:/Profile";
    }
}
