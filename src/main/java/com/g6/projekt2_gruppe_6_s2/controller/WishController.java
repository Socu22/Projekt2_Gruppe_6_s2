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
public class WishController {

    @Autowired
    WishRepositoryDataBase repo;

    //to see wich priority the wishes should be shown in, 0 is alpabetical, 1 is price
    static int prio = 0;

    //Getmappingen for a wish, takes a wishID and listId from wishlist html so it can find the wish in the database
    @GetMapping("/Wish")// change this to connect to a users list somehow
    public String Wish(HttpServletRequest request, @RequestParam("wishId") int wishId,@RequestParam("listId") int listId, Model model){
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
            session.setAttribute("id2", wishId);
        }
        model.addAttribute("isLoggedIn",user != null);
        boolean isUser = false;

        if (user!=null && repo.userOwnsList(user.getId(), listId)){
            isUser = true;
        }
        model.addAttribute("isUser",isUser );
        model.addAttribute("listId",listId);

        Wish wish;
        wish = repo.getWish(wishId);
        model.addAttribute("wish",wish);
        return "showWish";
    }
    //when you press the deletebutton in the showWish html you get send here, it deletes the wish and redirects you to the wislist you were just in
    @GetMapping("/removeWish")
    public String removeWish(HttpServletRequest request, Model model) throws SQLException {
        HttpSession session = request.getSession(false);
        User user = null;
        int lastActiveListId = 0;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
            int _id2 = (int) session.getAttribute("id2");
            lastActiveListId = (int) session.getAttribute("lastActiveWishList");
            repo.removeWishFromWishlist(_id2);
            UserController.getSession(request,model);
        }
        assert session != null;


        return "redirect:WishList?id="+lastActiveListId;
    }
    @GetMapping("/WishList")// shows wishes in a WishList
    public String WishList(@RequestParam("id") int id, HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
            session.setAttribute("lastActiveWishList",id);
            session.setAttribute("id",id);
            model.addAttribute("URL" ,request.getRequestURL().toString() + "?" + request.getQueryString());
        }
        model.addAttribute("isLoggedIn",user != null);

        boolean isUser = false;

        if (user!=null && repo.userOwnsList(user.getId(), id)){
            isUser = true;
        }
        model.addAttribute("isUser",isUser );


        var wishList = new WishList();
        wishList.setWishes(repo.getWishList(id ));
        wishList.setListId(id);


        model.addAttribute("wishList",wishList);
        model.addAttribute("priority" ,prio);
        assert session != null;



        return "wishList";
    }
    //if you press the alpahebtical button in wishList html, you get send here, the prio changes and it redirects you back
    @GetMapping("/alphabet")
    public String alphabet(@RequestParam("id") int listId, @RequestParam("prio") int prio, Model model,HttpServletRequest request){
        this.prio = prio;
        return "redirect:WishList?id="+listId;
    }
    //if you press the price button in wishList html, you get send here, the prio changes and it redirects you back
    @GetMapping("/price")
    public String price(@RequestParam("id") int listId, @RequestParam("prio") int prio, Model model,HttpServletRequest request){
        this.prio = prio;
        return "redirect:WishList?id="+listId;
    }

    @GetMapping("/getWishInWishList")
    public String getWishInWishList(Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null){
            UserController.getSession(request,model);
        }

        return "createWishInWishList";
    }
    @GetMapping("/saveWishInWishList")
    public String postWishInWishList(
            HttpServletRequest request,Model model,
            @RequestParam("price") String price,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("link") String link,
            @RequestParam("img") String img) throws SQLException {
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
        }

        assert session != null;
        int _id = (int) session.getAttribute("id");
        int lastActiveListId = (int) session.getAttribute("lastActiveWishList");

        model.addAttribute("id",_id);

        System.out.println("in postWishInWishList"+_id);

        double priceD;

        try
        {
            priceD = Double.parseDouble(price);
        }
        catch (NumberFormatException e)
        {
            priceD = .0;
        }

        ArrayList<Wish> wishList = new ArrayList<>();
        wishList.add(new Wish(title,description,img,priceD,link, repo.getNextWishId()));
        WishList wishListInstance = new WishList(title,_id,wishList);
        repo.saveWishlist(user.getId(), _id,wishListInstance);



        return "redirect:/WishList?id="+lastActiveListId;
    }
    // if you're owner of list and click the edit button on the showWish html you get here, it makes a new wish and then puts than on the wishlist and removes the old
    @GetMapping("/editWishInWishList")
    public String editWishInWishList(
            HttpServletRequest request,Model model,
            @RequestParam("wishId") int wishId,
            @RequestParam("listId") int listId,
            @RequestParam("price")double price,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("link") String link,
            @RequestParam("img") String img) throws SQLException {
        HttpSession session = request.getSession(false);
        User user = null;
        int lastActiveListId = 0;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
             lastActiveListId = (int) session.getAttribute("lastActiveWishList");
        }
        boolean isUser = false;

        if (user!=null && repo.userOwnsList(user.getId(), listId)){
            isUser = true;
        }




        if(isUser){
            repo.removeWishFromWishlist(wishId);
            ArrayList<Wish> wishList = new ArrayList<>();
            wishList.add(new Wish(title,description,img,price,link, repo.getNextWishId()));
            WishList wishListInstance = new WishList(title,listId,wishList);
            repo.saveWishlist(user.getId(), listId,wishListInstance);
        }
        return "redirect:WishList?id="+lastActiveListId;
    }


}
