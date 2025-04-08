package com.g6.projekt2_gruppe_6_s2.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;

import static org.apache.coyote.http11.Constants.a;

public class Wish{
    int id;
    String title;
    String description;
    String image;
    double price;
    String link;
    boolean prioritized;

    public Wish(String title, String description, String image, double price, String link, int id) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.link = link;
        this.id = id;
    }
    public Wish(String title) {
        this.title = title;
    }

    public Wish(int id) {
        this.id = id;
    }

    public Wish(int id, String title) {
        this.id = id;
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
    public String toString(){
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isPrioritized() {
        return prioritized;
    }

    public void setPrioritized(boolean prioritized) {
        this.prioritized = prioritized;
    }
}
