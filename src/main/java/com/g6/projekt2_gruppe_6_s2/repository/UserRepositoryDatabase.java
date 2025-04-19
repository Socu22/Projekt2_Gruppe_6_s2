package com.g6.projekt2_gruppe_6_s2.repository;

import com.g6.projekt2_gruppe_6_s2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.View;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class UserRepositoryDatabase
{
    @Autowired
    DataSource dataSource; // datasource wired to WishDatabase

    // method for getting all users in the database.
    // unused, but possibly useful if expanding functionality
    public ArrayList<User> getUsers()
            throws SQLException
    {
        ArrayList<User> userList = new ArrayList<>();

        String sql = "SELECT * FROM users"; // sql formulation

        // sql 'block' sending command to database
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next()) // loop 'constructing' users from returned data
            {
                User user = new User();

                user.setId      (resultSet.getInt   ("id"));
                user.setUsername(resultSet.getString("username"));
                //                                    not giving the passwords ;)
                user.setImgPath (resultSet.getString("img"));

                userList.add(user); // put user in arraylist
            }
        }
        catch (SQLException e) // catch sql-exceptions
        {
            e.printStackTrace(); // print stacktrace for troubleshooting

            // throw new exception with msg suitable to display for users
            throw new SQLException("Failed to Connect to Database.");
        }

        return userList;
    }

    // method to add a new user to database
    public User addUser(String username, String password)
            throws SQLException
    {
        // abort if user already exists, throwing exception with msg suitable to display for users.
        // the database itself does not strictly disallow users with same username, but it is enforced here.
        // this helps to identify and convey the specific error of the case that 'username' is already in use.
        if(hasUser(username)) throw new IllegalArgumentException("User already exists.");

        String sql = "INSERT INTO users (username, password) VALUE ( ? , ? )"; // sql template

        // sql 'block' formulating and sending command to database
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, username);
            statement.setString(2, password);

            statement.execute();
        }
        catch (SQLException e) // catch sql-exceptions
        {
            e.printStackTrace(); // print stacktrace for troubleshooting

            // throw new exception with msg suitable to display for users
            throw new SQLException("Failed to Connect to Database.");
        }

        // get newly established user from database.
        // possibly redundant, but new users are never established without being needed immediately
        return getUser(username, password);
    }

    // method for getting a user from the database, requires password for user security
    public User getUser(String username, String password)
            throws SQLException, IllegalArgumentException
    {
        User user;
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?"; // sql template

        // sql 'block' formulating and sending command to database
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, username);
            statement.setString(2, password);

            // 'staggered' try using the same catch, for setting up statement before execution,
            // while ensuring to close 'statement' afterward.
            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next()) // get and 'construct' user from returned data
                {
                    user = new User();

                    user.setId      (resultSet.getInt   ("userId"));
                    user.setUsername(resultSet.getString("username"));
                    //                                    not giving the password  ;)
                    user.setImgPath (resultSet.getString("img"));
                }
                else
                {
                    // in the case nothing is returned, either the username does not exist in database
                    // or given password is wrong. throws exception with msg suitable to display for users
                    throw new IllegalArgumentException("Incorrect Username or Password.");
                }
            }
        }
        catch (SQLException e) // catch sql-exceptions
        {
            e.printStackTrace(); // print stacktrace for troubleshooting

            // throw new exception with msg suitable to display for users
            throw new SQLException("Failed to Connect to Database.");
        }

        return user;
    }

    // method to check is a username is registered in the database.
    // as the database itself does not disallow entries with same username,
    // this method is for checking 'manually' to avoid such
    public boolean hasUser(String username)
            throws SQLException
    {
        String sql = "SELECT username FROM users WHERE username = ?"; // sql template

        // sql 'block' formulating and sending command to database
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, username);

            // 'staggered' try using the same catch, for setting up statement before execution,
            // while ensuring to close 'statement' afterward.
            try (ResultSet resultSet = statement.executeQuery())
            {
                // simply return true if any matching data is returned from database
                if (resultSet.next()) return true;
            }
        }
        catch (SQLException e) // catch sql-exceptions
        {
            e.printStackTrace(); // print stacktrace for troubleshooting

            // throw new exception with msg suitable to display for users
            throw new SQLException("Failed to Connect to Database.");
        }

        // return false is no data was returned from database and no exceptions occurred
        return false;
    }

    // method for removing a user from the database, requires full User instance and password for user security.
    // could be argued should return boolean or throw IllegalArgumentException, in case user does not exist,
    // but the existence of User instance implies user already exists.
    public void deleteUser(User user, String password)
            throws SQLException
    {
        // sql template
        String sql = "DELETE FROM users WHERE userId = ? AND username = ? AND password = ?";

        // sql 'block' formulating and sending command to database
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, "" + user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, password);
            statement.execute();
        }
        catch (SQLException e) // catch sql-exceptions
        {
            e.printStackTrace(); // print stacktrace for troubleshooting

            // throw new exception with msg suitable to display for users
            throw new SQLException("Failed to Connect to Database.");
        }
    }
}
