package com.g6.projekt2_gruppe_6_s2.repository;

import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class WishRepositoryDataBase {
    @Autowired
    DataSource dataSource;

    public ArrayList<Wish> getWishList(int listId) {
        ArrayList<Integer> wishIds = new ArrayList<Integer>();
        ArrayList<Wish> wishList = new ArrayList<>();
        String sql = "SELECT * FROM wishLists WHERE listId = " + listId + ";";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                wishIds.add(resultSet.getInt("wishId"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Integer wishId : wishIds) {
            sql = "SELECT * FROM wishes WHERE wishId = " + wishId + ";";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Wish wish = new Wish();
                    wish.setWish(resultSet.getString("title"));
                    wishList.add(wish);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return wishList;
    }
   public ArrayList<WishList> getWishLists(int userId) {
        ArrayList<WishList> wishLists = new ArrayList<>();
        String sql = "SELECT * FROM listHolders WHERE userId = " + userId + ";";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                WishList wishList = new WishList();
                wishList.setListId(resultSet.getInt("listId"));
                wishLists.add(wishList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishLists;
    }
}
