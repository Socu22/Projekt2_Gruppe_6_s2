package com.g6.projekt2_gruppe_6_s2.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;

import static org.apache.coyote.http11.Constants.a;

public class Wish{
    String title;
    String description;
    String image;
    double price;
    String link;
    boolean prioritized;

    public Wish(String title, String description, String image, double price, String link, boolean prioritized) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.link = link;
        this.prioritized = prioritized;
    }
    public Wish(String title) {
        this.title = title;
    }

    public Wish() {
    }
    public Wish(double wishPrice, String title, boolean prioritized) {price = wishPrice; this.title = title; this.prioritized = prioritized;}

    public String getWish() {
        return title;
    }

    public void setWish(String wish) {
        this.title = wish;
    }
    public String getTitle() {return title;}
}
