package com.g6.projekt2_gruppe_6_s2.model;

import java.util.ArrayList;

public class User {
    int id;
    String username;
    String password;

    ArrayList<WishList> wishLists;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public void setWishLists(ArrayList<WishList> wishLists) {
        this.wishLists = wishLists;
    }
}
