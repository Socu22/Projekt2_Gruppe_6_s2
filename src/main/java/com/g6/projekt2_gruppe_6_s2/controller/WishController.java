package com.g6.projekt2_gruppe_6_s2.controller;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepository;
import com.g6.projekt2_gruppe_6_s2.repository.WishRepositoryDataBase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
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



    @GetMapping("/Wish")// change this to connect to a users list somehow
    public String Wish(@RequestParam("id") int id, Model model){
        Wish wish;
        wish = repo.getWish(id);
        model.addAttribute("wish",wish);
        return "showWish";
    }
    @GetMapping("/WishList")// shows wishes in a WishList
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

    @GetMapping("/getWishInWishList")
    public String getWishInWishList(@RequestParam("id") int id, Model model){
        model.addAttribute("listId",id);//the id works until used here idk why. returns a 0 insead of a number


        return "createWishInWishList";
    }
    @GetMapping("/saveWishInWishList")
    public String postWishInWishList(HttpServletRequest request,
                                     @RequestParam("listId=id") int listId,
                                     @RequestParam("price")double price,
                                     @RequestParam("title") String title,
                                     @RequestParam("description") String description,
                                     @RequestParam("link") String link,
                                     @RequestParam("img") String img) throws SQLException {
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
        }

        System.out.println("in postWishInWishList"+listId);


        ArrayList<Wish> wishList = new ArrayList<>();
            wishList.add(new Wish(title,description,img,price,link, repo.getNextWishId()));
            WishList wishListInstance = new WishList(title,listId,wishList);
            repo.saveWishlist(user.getId(), listId,wishListInstance);


        return "redirect:/Profile";
    }



}
