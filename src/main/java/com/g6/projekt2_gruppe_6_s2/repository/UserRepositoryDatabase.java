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
    DataSource dataSource;

    @Autowired
    private View error;

    public ArrayList<User> getUsers()
            throws SQLException
    {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                User user = new User();

                user.setId      (resultSet.getInt   ("id"));
                user.setUsername(resultSet.getString("username"));
                //                                    not giving the passwords ;)
                user.setImgPath (resultSet.getString("img"));

                userList.add(user);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new SQLException("Failed to Connect to Database.");
        }

        return userList;
    }

    public User addUser(String username, String password)
            throws SQLException
    {
        // abort if user already exists
        if(hasUser(username)) throw new IllegalArgumentException("User already exists.");

        String sql = "INSERT INTO users (username, password) VALUE ( ? , ? )";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, username);
            statement.setString(2, password);

            statement.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new SQLException("Failed to Connect to Database.");
        }

        return getUser(username, password);
    }

    public User getUser(String username, String password)
            throws SQLException, IllegalArgumentException
    {
        User user;
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next())
                {
                    user = new User();

                    user.setId      (resultSet.getInt   ("userId"));
                    user.setUsername(resultSet.getString("username"));
                    //                                    not giving the password  ;)
                    user.setImgPath (resultSet.getString("img"));
                }
                else
                {
                    throw new IllegalArgumentException("Incorrect Username or Password.");
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new SQLException("Failed to Connect to Database.");
        }

        return user;
    }

    public boolean hasUser(String username)
            throws SQLException
    {
        String sql = "SELECT username FROM users WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next()) return true;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new SQLException("Failed to Connect to Database.");
        }

        return false;
    }

    public void deleteUser(User user, String password)
            throws SQLException
    {
        String sql = "DELETE FROM listHolders WHERE userId = ?;" +
                     "DELETE FROM users WHERE userId = ? AND username = ? AND password = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, ""+user.getId());
            statement.setString(2, ""+user.getId());
            statement.setString(3, user.getUsername());
            statement.setString(4, password);
            statement.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new SQLException("Failed to Connect to Database.");
        }
    }
}
