package com.g6.projekt2_gruppe_6_s2.model;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.text.Collator;
import java.util.ArrayList;

public class WishList {
    ArrayList<Wish> wishes = new ArrayList<>();

    public static void main(String[] args) {
        WishList wl = new WishList();
        wl.wishes.add(new Wish(600, "basd"));
        wl.wishes.add(new Wish(110, "adasd"));
        wl.wishes.add(new Wish(500,"esad"));
        wl.wishes.add(new Wish(740, "das"));
        wl.wishes.add(new Wish(100, "casd"));
        for (Wish w : wl.wishes) {
            System.out.println(w.price);
        }
        System.out.println();
        for (Wish w : wl.getListByPrice()) {
            System.out.println(w.price);
        }
        for (Wish w : wl.getAlphabeticalList()) {
            System.out.println(w.price);
        }

    }
    public ArrayList<Wish> getList(){
        return wishes;
    }
    public ArrayList<Wish> getAlphabeticalList(){
        ArrayList<Wish> newList = wishes;
        newList.sort(Collator.getInstance());
        return newList;
    }
    public ArrayList<Wish> getListByPrice(){
        ArrayList<Wish> newList = new ArrayList<>();
        for (int i = 0; i < wishes.size() ; i++){
            for (int j = 0; j < wishes.size(); j++){
                if (i == j) {
                    newList.add(wishes.get(i));
                    break;
                }
                if(wishes.get(i).price < newList.get(j).price){
                    newList.add(j, wishes.get(i));
                    break;
                }
            }
        }
        return newList;
    }
}
