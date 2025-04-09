-- Drop and create the database
DROP DATABASE IF EXISTS WishDatabase;
CREATE DATABASE WishDatabase;
USE WishDatabase;

-- Create the wishes table
CREATE TABLE wishes (
                        price INT,
                        wishId INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                        title VARCHAR(100) NOT NULL,
                        description VARCHAR(255),
                        link VARCHAR(255),
                        img VARCHAR(255)
);

-- Create the users table
CREATE TABLE users (
                       userId INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                       username VARCHAR(100) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       img VARCHAR(255)
);

-- Create the listHolders table
CREATE TABLE listHolders (
                             listId INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                             userId INT NOT NULL,
                             FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Create the wishLists table
CREATE TABLE wishLists (
                           listId INT,
                           wishId INT,
                           FOREIGN KEY (listId) REFERENCES listHolders(listId),
                           FOREIGN KEY (wishId) REFERENCES wishes(wishId)
);

-- Insert a user into the users table
INSERT INTO users (username, password, img)
VALUES ('user1', 'password1', 'http://userimage.com');

-- Insert wishes into the wishes table
INSERT INTO wishes (price, title, description, link, img)
VALUES
    (100, 'Wish 1', 'Description of Wish 1', 'http://link1.com', 'http://image1.com'),
    (200, 'Wish 2', 'Description of Wish 2', 'http://link2.com', 'http://image2.com'),
    (300, 'Wish 3', 'Description of Wish 3', 'http://link3.com', 'http://image3.com');

-- Create a wishlist for the user with userId = 1
INSERT INTO listHolders (userId)
VALUES (1);

-- Get the last inserted listId
SET @lastListId = LAST_INSERT_ID();

-- Associate the wishes with the wishlist
INSERT INTO wishLists (listId, wishId)
VALUES
    (@lastListId, 1),
    (@lastListId, 2),
    (@lastListId, 3);

-- Verify the data
SELECT * FROM wishes;
SELECT * FROM listHolders;
SELECT * FROM wishLists;
