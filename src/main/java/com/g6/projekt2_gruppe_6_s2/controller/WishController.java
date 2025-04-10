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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class WishController {
    
    @Autowired
    WishRepositoryDataBase repo;
    WishRepository repo2;

    @GetMapping("/Profile")// change this to connect to a users list somehow
    public String getIndex(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
           user = (User)session.getAttribute("activeUser");
        }



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
    public String postCreateWishlist(@RequestParam("title") String title ) throws SQLException {

        int listId = repo.createWishlist(1); //makes all necessary logic for creating something in this database. works with my lucas.sql

//        ArrayList<Wish> wishList = new ArrayList<>();
//        wishList.add(new Wish(repo.getNextWishId(),"t2"));
//        WishList wishListInstance = new WishList(title,listId,wishList);
//        repo.saveWishlist(1,listId,wishListInstance); //saves a dummy
        return "profile";
    }

    @GetMapping("/getWishInWishList")
    public String getWishInWishList(){
        return "createWishInWishList";
    }
    @GetMapping("/saveWishInWishList")
    public String postWishInWishList(@RequestParam("listId") int listId,@RequestParam("price")double price, @RequestParam("title") String title,@RequestParam("description") String description, @RequestParam("link") String link, @RequestParam("img") String img) throws SQLException {
        ArrayList<Wish> wishList = new ArrayList<>();
        wishList.add(new Wish(title,description,img,price,link, repo.getNextWishId()));
        WishList wishListInstance = new WishList(title,listId,wishList);
        repo.saveWishlist(1,listId,wishListInstance);

        return "profile";
    }



}
