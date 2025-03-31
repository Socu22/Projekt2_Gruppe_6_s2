package com.g6.projekt2_gruppe_6_s2.config;

import com.g6.projekt2_gruppe_6_s2.model.Wish;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Component
public class InitData {
    ArrayList<Wish> wishList = new ArrayList<>();

    public InitData() {
        wishList.add(new Wish("help111"));
        wishList.add(new Wish("help122"));
        wishList.add(new Wish("help133"));
        wishList.add(new Wish("help66"));
    }

    public ArrayList<Wish> getWishList() {
        return wishList;
    }
}
