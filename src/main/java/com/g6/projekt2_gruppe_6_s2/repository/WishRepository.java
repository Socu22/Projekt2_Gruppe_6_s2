package com.g6.projekt2_gruppe_6_s2.repository;

import com.g6.projekt2_gruppe_6_s2.config.InitData;
import com.g6.projekt2_gruppe_6_s2.model.Wish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class WishRepository {
    @Autowired
    InitData initData;

    public ArrayList<Wish> getWishList() {
        return initData.getWishList();
    }
}
