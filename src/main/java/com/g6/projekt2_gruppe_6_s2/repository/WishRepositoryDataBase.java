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
                    Wish wish = new Wish(resultSet.getString("title"),resultSet.getString("description"),
                            resultSet.getString("img"),resultSet.getInt("price"),
                            resultSet.getString("link"), resultSet.getInt("wishId"));

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
    public Wish getWish(int wishId) {
        Wish wish = new Wish();
        String sql = "SELECT * FROM wishes WHERE wishId = " + wishId + ";";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                wish = new Wish(resultSet.getString("title"),resultSet.getString("description"),
                        resultSet.getString("img"),resultSet.getInt("price"),
                        resultSet.getString("link"), resultSet.getInt("wishId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wish;
    }

    // Create a new wishlist and associate wishes with it
    public int createWishlist(int userId) throws SQLException {
        String insertListHolderSQL = "INSERT INTO listHolders (userId) VALUES (?)";
        String insertWishListSQL = "INSERT INTO wishLists (listId, wishId) VALUES (?, ?)";
        String insertWishesSQL = "INSERT INTO wishes (title) VALUES (?)";

        // Create a new wish with a unique title
        String wishTitle = "wishestest";
        int listId = 0;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement listHolderStmt = conn.prepareStatement(insertListHolderSQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement wishListStmt = conn.prepareStatement(insertWishListSQL);
             PreparedStatement wishesStmt = conn.prepareStatement(insertWishesSQL, Statement.RETURN_GENERATED_KEYS)) {

            // Insert into listHolders
            listHolderStmt.setInt(1, userId);
            listHolderStmt.executeUpdate();

            // Get the generated listId
            try (ResultSet generatedKeys = listHolderStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    listId = generatedKeys.getInt(1);

                    // Insert the wish into the wishes table
                    wishesStmt.setString(1, wishTitle);
                    wishesStmt.executeUpdate();

                    // Get the generated wishId
                    try (ResultSet wishKeys = wishesStmt.getGeneratedKeys()) {
                        if (wishKeys.next()) {
                            int wishId = wishKeys.getInt(1);

                            // Insert the wish into the wishLists table
                            wishListStmt.setInt(1, listId);
                            wishListStmt.setInt(2, wishId);
                            wishListStmt.executeUpdate();
                        }
                    }
                }
            }
        }
        return listId;
    }


    public int getNextWishId() throws SQLException {
        String selectMaxWishIdSQL = "SELECT MAX(wishId) FROM wishes";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectMaxWishIdSQL);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int maxWishId = rs.getInt(1);
                return maxWishId + 1; // Return the next available wishId
            } else {
                return 1; // If no wishes exist, start with 1
            }
        }
    }


    // Save changes to the database
    public void saveWishlist(int userId, WishList usersWishList) throws SQLException {
        String selectListHolderSQL = "SELECT listId FROM listHolders WHERE userId = ?";
        String insertWishListSQL = "INSERT INTO wishLists (listId, wishId) VALUES (?, ?)";
        String insertWishesSQL = "INSERT INTO wishes (wishId, title) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement listHolderStmt = conn.prepareStatement(selectListHolderSQL);
             PreparedStatement wishListStmt = conn.prepareStatement(insertWishListSQL);
             PreparedStatement wishesStmt = conn.prepareStatement(insertWishesSQL)) {

            listHolderStmt.setInt(1, userId);
            try (ResultSet resultSet = listHolderStmt.executeQuery()) {
                if (resultSet.next()) {
                    int listId = resultSet.getInt("listId");

                    // Assuming wishIds is a field in usersWishList
                    for (int wishId : usersWishList.getWishesIds()) {
                        wishListStmt.setInt(1, listId);
                        wishListStmt.setInt(2, wishId);
                        wishListStmt.addBatch();

                        wishesStmt.setInt(1, wishId);
                        wishesStmt.setString(2, "wishestest");
                        wishesStmt.addBatch();
                    }
                    wishListStmt.executeBatch();
                    wishesStmt.executeBatch();
                } else {
                    throw new SQLException("No listId found for userId: " + userId);
                }
            }
        }
    }
    // Add wishes to an existing wishlist ------- outdated
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

    // Remove wishes from an existing wishlist ------outdated
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

    // Delete a wishlist and its associations ---- outdated
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


    // outdated
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
