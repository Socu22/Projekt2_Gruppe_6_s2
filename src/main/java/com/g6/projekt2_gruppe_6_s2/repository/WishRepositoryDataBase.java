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

@Repository  //This is an annotation which tells this Spring boot project that it is to be handled like a database/repository
public class WishRepositoryDataBase {
    @Autowired //Makes sure that one instance of the DataSource is created.
    DataSource dataSource; // this DataSource is a direct connection to the jdbc database connected through the springboot project. Read readme if you want more information about how it is set up.
    //takes a listID and checks the database for all the wishes in that list and adds them in a wishList and returns it
    public ArrayList<Wish> getWishList(int listId) { // the unique listId is to be put in parameter to be found.
        ArrayList<Integer> wishIds = new ArrayList<>();
        ArrayList<Wish> wishList = new ArrayList<>();
        String sql = "SELECT * FROM wishLists WHERE listId = " + listId + ";"; // selects all wishlists with the specific listId

        try (Connection connection = dataSource.getConnection(); // connects to database
             PreparedStatement statement = connection.prepareStatement(sql); //sql injection protected mysql statements
             ResultSet resultSet = statement.executeQuery()) { //the way to get results through a database.
            while (resultSet.next()) {// adds all wishIds as long that there is a next one.
                wishIds.add(resultSet.getInt("wishId"));
            }

        } catch (SQLException e) { // in case of exception
            e.printStackTrace();
        }
        for (Integer wishId : wishIds) { // uses wishId's number of ids to determine the number of wishes in list
            sql = "SELECT * FROM wishes WHERE wishId = " + wishId + ";";
            try (Connection connection = dataSource.getConnection(); // connection to database
                 PreparedStatement statement = connection.prepareStatement(sql); // protected sql statement
                 ResultSet resultSet = statement.executeQuery()) { // results
                while (resultSet.next()) { // as long that there is a next wish
                    Wish wish = new Wish( // adds all things into a constructor through resultSet
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getString("img"),resultSet.getInt("price"),
                            resultSet.getString("link"), resultSet.getInt("wishId"));

                    wishList.add(wish); // adds the wish to wishList
                }
            } catch (SQLException e) { // exception
                e.printStackTrace();
            }
        }
        return wishList; // returns wishlist
    }
    //takes a userID and checks in the database for all the wishlists that user has and adds them to a list and returns it.
    public ArrayList<WishList> getWishLists(int userId) { //same concept as the getWishList(int listId)
        ArrayList<WishList> wishListsList = new ArrayList<>();
        String sql = "SELECT * FROM listHolders WHERE userId = " + userId + ";"; //gets list connected to a user.

        try (Connection connection = dataSource.getConnection(); // connection to database
             PreparedStatement statement = connection.prepareStatement(sql); // sql injection protected
             ResultSet resultSet = statement.executeQuery()) { // results

            while (resultSet.next()) { // if there is a next results do this
                WishList wishList = new WishList();
                wishList.setListId(resultSet.getInt("listId")); // sets the listId
                wishList.setTitle(resultSet.getString("title")); //sets the Title
                wishListsList.add(wishList); // adds to wishListsList
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishListsList; //returns the list of wishLists
    }
    //takes a wishId and finds all the variables for that wish in the database and ads them to a wish and returns it.
    public Wish getWish(int wishId) { // same concept as getWishList.
        Wish wish = new Wish();
        String sql = "SELECT * FROM wishes WHERE wishId = " + wishId + ";"; // selects one wish
        try (Connection connection = dataSource.getConnection(); // connection to database
             PreparedStatement statement = connection.prepareStatement(sql); // sql injection protected
             ResultSet resultSet = statement.executeQuery()) { // results

            while (resultSet.next()) { // we know there is always only one wish with one specific wishId therefore we can afford to do it this way. Look at our setup for our database, if you want info about it.
                wish = new Wish( // adds with resultSet the results into a wish
                        resultSet.getString("title"),resultSet.getString("description"),
                        resultSet.getString("img"),resultSet.getInt("price"),
                        resultSet.getString("link"), resultSet.getInt("wishId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wish; // returns wish
    }
    //takes an userid and a listid and checks if the list is owned by the user in the database, if it is it returns true, else false
    public boolean userOwnsList(int userId, int listId) { // this is for checking if you actually own a list.
        String sql = "SELECT * FROM listHolders WHERE listId = " + listId + ";"; // first it checks for list holders who hold the listId
        try (Connection connection = dataSource.getConnection(); //connection
             PreparedStatement statement = connection.prepareStatement(sql); //protection sql
             ResultSet resultSet = statement.executeQuery()) { // results

            while (resultSet.next()) {
                if (resultSet.getInt("userId") == userId) { // if the listHolder who holds the list is the correct user it returns true otherwise it returns false.
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // in case it is not a match.
    }
    // Create a new wishlist, and create all necessary database stuff also.
    public int createWishlist(int userId, String title) throws SQLException //What the method has to know.
    {
        String insertListHolderSQL = "INSERT INTO listHolders (userId, title) VALUES (?,?)"; // this makes the listholder. a listholder is the way we prove that there exist a list connected to a user in the database
        int listId = 0; // this returns the listId so it can be used in the controller. it is a bit outdated but that is how the method is made.

        try (Connection conn = dataSource.getConnection();
             PreparedStatement listHolderStmt = conn.prepareStatement(insertListHolderSQL, Statement.RETURN_GENERATED_KEYS);
             )
        {  // Insert into listHolders
            listHolderStmt.setInt(1, userId);
            listHolderStmt.setString(2, title);
            listHolderStmt.executeUpdate();

            // Get the generated listId
            try (ResultSet generatedKeys = listHolderStmt.getGeneratedKeys())
            {
                if (generatedKeys.next())
                {
                    listId = generatedKeys.getInt(1); // listId that gets returned
                }
            }
        }
        return listId; // returns listId
    }
    //  this gets the next wishId, as the wishId is auto generation there is no way for the user to know which wishId is available.
    public int getNextWishId() throws SQLException {
        String selectMaxWishIdSQL = "SELECT MAX(wishId) FROM wishes"; //sees the max number, otherwise called the latest and biggest number of all wishes

        try (Connection conn = dataSource.getConnection(); // connection to database
             PreparedStatement stmt = conn.prepareStatement(selectMaxWishIdSQL); // sql injection protection
             ResultSet rs = stmt.executeQuery()) { // results

            if (rs.next()) { // this is one number but this is just the way to get a result out of the database
                int maxWishId = rs.getInt(1); // selects the columnindex wishId in wishes.
                return maxWishId + 1; // Return the next available wishId, which is the current max +1
            } else {
                return 1; // If no wishes exist, start with 1
            }
        }
    }
    // Save changes to the database - kind of depending on listId from createwislist method
    public void saveWishlist(int userId, int _listId, WishList objectWishList) throws SQLException { // needs a user and listId to put something the right place. and then the wishList object that has an arraylist of wishes inside itself to store wishes.
        String selectListHolderSQL = "SELECT listId FROM listHolders WHERE userId = ? AND listId = ?"; // this is to get the current listholder for the current user and listId
        String insertWishesSQL = "INSERT INTO wishes (price, title, description, link, img) VALUES (?,?,?,?,?)"; //this insert into the place in the database where all wishes is stored
        String insertWishListSQL = "INSERT INTO wishLists (listId, wishId) VALUES (?, ?)"; // this is to make a connection between a list from listholder and the created wishes

        try (Connection conn = dataSource.getConnection(); // connection to database
             PreparedStatement listHolderStmt = conn.prepareStatement(selectListHolderSQL); //sql injection safe
             PreparedStatement wishesStmt = conn.prepareStatement(insertWishesSQL);//sql injection safe
             PreparedStatement wishListStmt = conn.prepareStatement(insertWishListSQL)) {//sql injection safe

            List<Integer> wishIds = objectWishList.getWishesIds(); // this is to get a list of the current lists wishIds
            List<Wish> tempWishList = objectWishList.getList(); // this is to get all wishes

            // first we begin with a listholder.
            listHolderStmt.setInt(1, userId); // sets the first ? from the sql
            listHolderStmt.setInt(2, _listId); // sets the second ? from the sql

            try (ResultSet resultSet = listHolderStmt.executeQuery()) {// gets the results
                if (resultSet.next()) { // for each listId. there is only one here and can be only one
                    int listId = resultSet.getInt("listId"); // gets the listId. though naming the right name made in listHolder table in database.

                    for (int i = 0; i < wishIds.size(); i++) { // by using the size of wishIds i can know how many wishes there is in the list
                        Wish currentWish = tempWishList.get(i);// gets a wish based on current id in the i.

                        // sets attributes for the wish in the wishes Table.
                        wishesStmt.setDouble(1, currentWish.getPrice());
                        wishesStmt.setString(2, currentWish.getTitle());
                        wishesStmt.setString(3, currentWish.getDescription());
                        wishesStmt.setString(4, currentWish.getLink());
                        wishesStmt.setString(5, currentWish.getImage());
                        wishesStmt.addBatch(); // adds to a batch
                    }
                    wishesStmt.executeBatch(); // executes all current batches stored in the statement.addBatch() at once.

                    for (int wishId : wishIds) { // for each wish there also needs to be created a connection between the wishes and a wishList connected to listHolders. as wishLists is a fully foreign keys it has to made lastly
                        wishListStmt.setInt(1, listId);
                        wishListStmt.setInt(2, wishId);
                        wishListStmt.addBatch(); // the same
                    }
                    wishListStmt.executeBatch(); // all at once
                } else {
                    throw new SQLException("No listId found for userId: " + userId); // in case of inputMismatch
                }
            }
        }
    }
    // Delete a wishlist and its associations
    public void deleteWishlist(int listId) throws SQLException { // the same as some of the earlier methods, it just needs to know listId
        String deleteWishListSQL = "DELETE FROM wishLists WHERE listId = ?"; // deletes all wishLists with foreign keys that ties to other listholders
        String deleteListHolderSQL = "DELETE FROM listHolders WHERE listId = ?"; //deletes listHolders when there is no wishList that connects to a wish

        try (Connection conn = dataSource.getConnection(); // connection to database
             PreparedStatement wishListStmt = conn.prepareStatement(deleteWishListSQL); //sql injection safe
             PreparedStatement listHolderStmt = conn.prepareStatement(deleteListHolderSQL)) {//sql injection safe

            // Delete from wishLists
            wishListStmt.setInt(1, listId);
            wishListStmt.executeUpdate();

            // Delete from listHolders
            listHolderStmt.setInt(1, listId);
            listHolderStmt.executeUpdate();
        }
    }
    // Remove wishes from an existing wishlist
    public void removeWishFromWishlist(int wishId) throws SQLException {
        String deleteWishListSQL = "DELETE FROM wishLists WHERE wishId = ?";// deletes a wishList with ?

        try (Connection conn = dataSource.getConnection(); //connection to database
             PreparedStatement wishListStmt = conn.prepareStatement(deleteWishListSQL)) {//sql injection safe

            wishListStmt.setInt(1, wishId); // puts wishId into the ?
            wishListStmt.executeUpdate(); // saves the updates
        }
    }
}
