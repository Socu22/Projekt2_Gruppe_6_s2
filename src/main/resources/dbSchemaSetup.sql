
DROP DATABASE IF EXISTS WishDatabase;

CREATE DATABASE WishDatabase;
USE WishDatabase;

# Table of users
CREATE TABLE users
(
    id INT   AUTO_INCREMENT PRIMARY KEY NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    img      VARCHAR(255)
);

# Table associating user.id to wishlists
CREATE TABLE listsKeys
(
    list INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    user INT NOT NULL, FOREIGN KEY (user) references users(id)
);

# Table of each wish
CREATE TABLE wishes
(
    price INT,
    id    INT   AUTO_INCREMENT PRIMARY KEY NOT NULL,
    title       VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    link        VARCHAR(255),
    img         VARCHAR(255)
);

# Table of associations wishlist-wish
CREATE TABLE wishList
(
    list INT, FOREIGN KEY (list) references listsKeys(list),
    wish INT, FOREIGN KEY (wish) references wishes(id)
);
