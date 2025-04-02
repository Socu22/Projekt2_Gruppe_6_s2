
DROP DATABASE IF EXISTS WishDatabase;

CREATE DATABASE WishDatabase;
USE WishDatabase;

# Table of each wish
CREATE TABLE wishes
(
    price       INT,
    wishId      INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    title       VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    link        VARCHAR(255),
    img         VARCHAR(255)
);

# Table of users
CREATE TABLE users
(
    userId   INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    img      VARCHAR(255)
);

# Table associating users to wishlists
CREATE TABLE listHolders
(
    listId INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    userId INT NOT NULL, FOREIGN KEY (userId) references users(userId)
);

# Table associating wishes to wishlists
CREATE TABLE wishLists
(
    listId INT, FOREIGN KEY (listId) references listHolders(listId),
    wishId INT, FOREIGN KEY (wishId) references wishes(wishId)
);
