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
    @GetMapping("/Wish")// show one wish
    public String Wish(HttpServletRequest request, @RequestParam("wishId") int wishId,@RequestParam("listId") int listId, Model model){ // here is requested wishId & listId
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
            session.setAttribute("id2", wishId); // this is wishId
        }
        model.addAttribute("isLoggedIn",user != null); // this makes sure that user is not null when logged in
        boolean isUser = false;

        if (user!=null && repo.userOwnsList(user.getId(), listId)){
            isUser = true; // user exist in session. it is a confirmation
        }
        model.addAttribute("isUser",isUser ); // sends the attribute to template
        model.addAttribute("listId",listId); //  sends the attribute to template

        Wish wish;
        wish = repo.getWish(wishId); // gets wishId based on the requested attribute
        model.addAttribute("wish",wish); // sends the attribute to template
        return "showWish";
    }
    //when you press the deletebutton in the showWish html you get send here, it deletes the wish and redirects you to the wislist you were just in
    @GetMapping("/removeWish")
    public String removeWish(HttpServletRequest request, Model model) throws SQLException {
        HttpSession session = request.getSession(false);
        User user = null;
        int lastActiveListId = 0; // last active listId
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
            int _id2 = (int) session.getAttribute("id2"); // wishId
            lastActiveListId = (int) session.getAttribute("lastActiveWishList"); // WishId saved in seesion
            repo.removeWishFromWishlist(_id2); // remove a Wish in wishList
            UserController.getSession(request,model); //makes sure that the user is logged in. for html logic
        }
        assert session != null;


        return "redirect:WishList?id="+lastActiveListId; // redirect to last known WishList id. the page before you entered a wish and deleted it.
    }
    @GetMapping("/WishList")// shows wishes in a WishList
    public String WishList(@RequestParam("id") int id, HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        User user = null;
        if(session!=null){
            user = (User)session.getAttribute("activeUser");
            session.setAttribute("lastActiveWishList",id);
            session.setAttribute("id",id); //listId is set
            model.addAttribute("URL" ,request.getRequestURL().toString() + "?" + request.getQueryString()); // current url without seesion. it can be shown to nonusers
        }
        model.addAttribute("isLoggedIn",user != null);// is logged in

        boolean isUser = false;

        if (user!=null && repo.userOwnsList(user.getId(), id)){
            isUser = true;
        }
        model.addAttribute("isUser",isUser ); // sends to template


        var wishList = new WishList();
        wishList.setWishes(repo.getWishList(id )); // gets all wishes from a specific list specified "id", which is a listID
        wishList.setListId(id); // set a wishList to have the listId inside it


        model.addAttribute("wishList",wishList); // sends to template
        model.addAttribute("priority" ,prio); // sends to template
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


    // the site where you inset the values for the wish in the Wishlist
    @GetMapping("/getWishInWishList")
    public String getWishInWishList(Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null){
            UserController.getSession(request,model); // makes sure the user is the correct user.
        }

        return "createWishInWishList"; // the html
    }


    // saves the Wish in Wishlist
    @GetMapping("/saveWishInWishList")
    public String postWishInWishList( // requests from the html
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
        int _id = (int) session.getAttribute("id"); // listId
        int lastActiveListId = (int) session.getAttribute("lastActiveWishList"); //Is sure to be the last known list before wishing to post a wish in a wishlist

        model.addAttribute("id",_id); // sends to template


        double priceD;

        try
        {
            priceD = Double.parseDouble(price); // price parsed to double
        }
        catch (NumberFormatException e)
        {
            priceD = .0;
        }

        ArrayList<Wish> wishList = new ArrayList<>();
        wishList.add(new Wish(title,description,img,priceD,link, repo.getNextWishId())); // ads to wishlist arraylist
        WishList wishListInstance = new WishList(title,_id,wishList); // adds a title listId, wishlist to Wishlist model
        repo.saveWishlist(user.getId(), _id,wishListInstance); // saves all the stuff to the right wishlist. it will communicate with the database



        return "redirect:/WishList?id="+lastActiveListId; // the current wishList
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
