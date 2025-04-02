package com.g6.projekt2_gruppe_6_s2.model;

import org.springframework.beans.factory.annotation.Autowired;

public class Wish {
    String title;
    String description;
    String image;
    double price;

    public Wish(String title, String description, String image, double price) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
    }
    public Wish(String title) {
        this.title = title;
    }

    public Wish() {
    }
    public Wish(double wishPrice, String title) {price = wishPrice; this.title = title;}

    public String getWish() {
        return title;
    }

    public void setWish(String wish) {
        this.title = wish;
    }

}
