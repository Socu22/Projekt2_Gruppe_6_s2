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
public class UserRepository {
    @Autowired
    DataSource dataSource;
    @Autowired
    private View error;

    public ArrayList<User> getUsers() {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setImgPath(resultSet.getString("imgPath"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }
    public User getUserWithUsernamePassword(String username, String password){
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ? and password=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setImgPath(resultSet.getString("img"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

}
