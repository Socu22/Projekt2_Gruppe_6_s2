package com.g6.projekt2_gruppe_6_s2.repository;

import com.g6.projekt2_gruppe_6_s2.model.Wish;
import com.g6.projekt2_gruppe_6_s2.model.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WishRepositoryDataBase {
    @Autowired
    DataSource dataSource;

    public ArrayList<Wish> getWishList(int listId) {
        ArrayList<Integer> wishIds = new ArrayList<>();
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

    // Create a new wishlist and associate wishes with it
    public void createWishlist(int userId, List<Integer> wishIds) throws SQLException {
        String insertListHolderSQL = "INSERT INTO listHolders (userId) VALUES (?)";
        String insertWishListSQL = "INSERT INTO wishLists (listId, wishId) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement listHolderStmt = conn.prepareStatement(insertListHolderSQL,
                     Statement.RETURN_GENERATED_KEYS);
             PreparedStatement wishListStmt = conn.prepareStatement(insertWishListSQL)) {

            // Insert into listHolders
            listHolderStmt.setInt(1, userId);
            listHolderStmt.executeUpdate();

            // Get the generated listId
            try (ResultSet generatedKeys = listHolderStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int listId = generatedKeys.getInt(1);

                    // Insert wishes into wishLists
                    for (int wishId : wishIds) {
                        wishListStmt.setInt(1, listId);
                        wishListStmt.setInt(2, wishId);
                        wishListStmt.addBatch();
                    }
                    wishListStmt.executeBatch();
                }
            }
        }
    }
    // Save changes to the database
    public void saveWishlist(int listId, List<Integer> wishIdsToAdd, List<Integer> wishIdsToRemove) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false); // Start a transaction
            try {
                // Remove wishes
                removeWishesFromWishlist(listId, wishIdsToRemove);

                // Add wishes
                addWishesToWishlist(listId, wishIdsToAdd);

                conn.commit(); // Commit the transaction
            } catch (SQLException e) {
                conn.rollback(); // Rollback in case of an error
                throw e;
            }
        }
    }

    // Add wishes to an existing wishlist
    public void addWishesToWishlist(int listId, List<Integer> wishIds) throws SQLException {
        String insertWishListSQL = "INSERT INTO wishLists (listId, wishId) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement wishListStmt = conn.prepareStatement(insertWishListSQL)) {

            for (int wishId : wishIds) {
                wishListStmt.setInt(1, listId);
                wishListStmt.setInt(2, wishId);
                wishListStmt.addBatch();
            }
            wishListStmt.executeBatch();
        }
    }

    // Remove wishes from an existing wishlist
    public void removeWishesFromWishlist(int listId, List<Integer> wishIds) throws SQLException {
        String deleteWishListSQL = "DELETE FROM wishLists WHERE listId = ? AND wishId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement wishListStmt = conn.prepareStatement(deleteWishListSQL)) {

            for (int wishId : wishIds) {
                wishListStmt.setInt(1, listId);
                wishListStmt.setInt(2, wishId);
                wishListStmt.addBatch();
            }
            wishListStmt.executeBatch();
        }
    }

    // Delete a wishlist and its associations
    public void deleteWishlist(int listId) throws SQLException {
        String deleteWishListSQL = "DELETE FROM wishLists WHERE listId = ?";
        String deleteListHolderSQL = "DELETE FROM listHolders WHERE listId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement wishListStmt = conn.prepareStatement(deleteWishListSQL);
             PreparedStatement listHolderStmt = conn.prepareStatement(deleteListHolderSQL)) {

            // Delete from wishLists
            wishListStmt.setInt(1, listId);
            wishListStmt.executeUpdate();

            // Delete from listHolders
            listHolderStmt.setInt(1, listId);
            listHolderStmt.executeUpdate();
        }
    }
    public List<Map<String, Object>> showWishlist(int listId) throws SQLException {
        String selectWishListSQL = "SELECT w.wishId, w.title, w.description, w.price, w.link, w.img " +
                "FROM wishLists wl " +
                "JOIN wishes w ON wl.wishId = w.wishId " +
                "WHERE wl.listId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement wishListStmt = conn.prepareStatement(selectWishListSQL)) {

            wishListStmt.setInt(1, listId);
            try (ResultSet rs = wishListStmt.executeQuery()) {
                List<Map<String, Object>> wishes = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> wish = new HashMap<>();
                    wish.put("wishId", rs.getInt("wishId"));
                    wish.put("title", rs.getString("title"));
                    wish.put("description", rs.getString("description"));
                    wish.put("price", rs.getInt("price"));
                    wish.put("link", rs.getString("link"));
                    wish.put("img", rs.getString("img"));
                    wishes.add(wish);
                }
                return wishes;
            }
        }
    }








}
