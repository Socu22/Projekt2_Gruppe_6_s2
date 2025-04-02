package com.g6.projekt2_gruppe_6_s2.model;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class WishList {
    String title;

    ArrayList<Wish> wishes = new ArrayList<>();

    public static void main(String[] args) {
        WishList wl = new WishList();
        wl.wishes.add(new Wish(600, "basd", true));
        wl.wishes.add(new Wish(110, "adasd", false));
        wl.wishes.add(new Wish(500,"esad", true));
        wl.wishes.add(new Wish(740, "dcas", true));
        wl.wishes.add(new Wish(100, "dbasd", false));
        for (Wish w : wl.wishes) {
            System.out.println(w.price);
        }
        System.out.println();
        for (Wish w : wl.getListByPrice()) {
            System.out.println(w.price);
        }
        for (Wish w : wl.getAlphabeticalList()) {
            System.out.println(w.title);
        }
        for (Wish w : wl.getListByPreference()) {
            System.out.println(w.prioritized);
        }

    }
    public ArrayList<Wish> getList(){
        return wishes;
    }
    public ArrayList<Wish> getAlphabeticalList() {
        ArrayList<Wish> newList = wishes;

        Collator collator = Collator.getInstance(Locale.getDefault());
        newList.sort((w1, w2) -> collator.compare(w1.getTitle().toLowerCase(), w2.getTitle().toLowerCase()));

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
    public ArrayList<Wish> getListByPreference(){
        ArrayList<Wish> newList = getAlphabeticalList();
        ArrayList<Wish> pList = new ArrayList<>();
        ArrayList<Wish> notPList = new ArrayList<>();
        for (int i = 0; i < newList.size() ; i++){
            if(newList.get(i).prioritized)
                pList.add(newList.get(i));
            else
                notPList.add(newList.get(i));
        }
        pList.addAll(notPList);
        return pList;
    }
}
