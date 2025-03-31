package com.g6.projekt2_gruppe_6_s2.model;

import org.springframework.beans.factory.annotation.Autowired;

public class Wish {
    String wish;
    public Wish(String wish) {
        this.wish = wish;
    }

    public Wish() {
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }
}
