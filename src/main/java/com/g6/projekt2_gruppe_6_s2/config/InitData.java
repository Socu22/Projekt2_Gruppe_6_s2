package com.g6.projekt2_gruppe_6_s2.config;

import com.g6.projekt2_gruppe_6_s2.model.User;
import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Component
public class InitData {
    ArrayList<WishList> wishLists = new ArrayList<>();

    public InitData() {
        /*wishLists.add(new WishList("bluse"));
        wishLists.add(new WishList("høretelefoner"));
        wishLists.add(new WishList("dildo"));
        wishLists.add(new WishList("sweater"));*/
    }

    public ArrayList<WishList> getWishLists() {
        return wishLists;
    }
}
