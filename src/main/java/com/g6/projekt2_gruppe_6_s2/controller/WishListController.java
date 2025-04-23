package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
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
    // shows wishLists Based on current activeUser, if you're not logged in it shows no wishlists
    @GetMapping("/Profile")
    public String getIndex(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");

        }
        model.addAttribute("isLoggedIn",user != null);


        var wishLists = new ArrayList<WishList>();
        if(user!=null){
            wishLists.addAll(repo.getWishLists(user.getId())); // gets all WishLists of the model "WishList"
            for (int i = 0; i < wishLists.size(); i++) { // i is the current WishList
                wishLists.get(i).setWishes(repo.getWishList(wishLists.get(i).getListId()));

                ArrayList<Wish> list = wishLists.get(i).getWishes();
                while (list.size() > 3) { // deletes a wish until 3 elements
                    list.remove(list.size() - 1);
                }
                wishLists.get(i).setWishes(list); // sets the wishes to the 3 elements
            }
            model.addAttribute("wishLists",wishLists);
        }


        return "profile";
    }


        // The page for inserting the title
    @GetMapping("/getCreateWishList")
    public String getCreateWishList(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session!=null){
            UserController.getSession(request,model);
        }
        return "createWishList";
    }


    // the Logic for when you insert a title, and the methods calls to make related things in the database

    @GetMapping("/saveCreateWishList")
    public String postCreateWishlist(HttpServletRequest request, @RequestParam("title") String title ) throws SQLException {

        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
        }

      repo.createWishlist(user.getId(),title); //makes all necessary logic for creating something in this database. works with my lucas.sql
        return "redirect:/Profile";
    }


    // for a button to be able to del a wishlist from an saved listId saved in session as id
    @GetMapping("/deleteWishList")
    public String deleteWishList(HttpServletRequest request) throws SQLException {
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
        }
        assert session != null;
        int listId = (int) session.getAttribute("id"); // here is the id

        repo.deleteWishlist(listId); // del based on id


        return "redirect:/Profile"; // redirect to profiles which views all current users wishlists
    }

}
