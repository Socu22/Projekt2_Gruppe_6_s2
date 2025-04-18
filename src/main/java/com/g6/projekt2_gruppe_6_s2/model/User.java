package com.g6.projekt2_gruppe_6_s2.model;

public class User {
    int id;
    String username;
    String password;
    String imgPath;

    //just a constructor
    public User(int id, String username, String password, String imgPath) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.imgPath = imgPath;
    }

    public User() {
    }
    //different getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
