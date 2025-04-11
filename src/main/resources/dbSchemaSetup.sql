
DROP DATABASE IF EXISTS WishDatabase;

CREATE DATABASE WishDatabase;
USE WishDatabase;

#   GDPR notes:
# While person-identifying data is never asked from users by our app, the
# tables 'users' and 'listHolders' can be used to identify a user within
# our system.
# This, along with cookies and ip traffic, can then be used to identify
# persons. Therefore, the information stored in these tables must be
# considered as person-identifying data, and should be accessible to the
# associated user for retrieval and deletion

# Table of every wish in database
CREATE TABLE wishes
(
    wishId      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    img         VARCHAR(255),
    link        VARCHAR(255),
    price       INT
);

# Table of users
CREATE TABLE users
(
    userId   INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    img      VARCHAR(255) # Optional profile picture
);

# Table associating users to wishlists
CREATE TABLE listHolders
(
    # Internal list identifier
    listId INT NOT NULL  AUTO_INCREMENT PRIMARY KEY,

    # User owning the list (if the user is deleted this entry is also deleted)
    userId INT NOT NULL, FOREIGN KEY (userId) references users(userId) ON DELETE CASCADE,

    # Optional title and image allowing easier distinction for users with multiple lists
    title  VARCHAR(100),
    img    VARCHAR(255)
);

# Table associating wishes to wishlists
CREATE TABLE wishLists
(
    # List holding this entry (if the list is deleted this entry is also deleted)
    listId INT NOT NULL, FOREIGN KEY (listId) references listHolders(listId) ON DELETE CASCADE,

    # Wish attributed to this entry, and thereby List (if the user is deleted this entry is also deleted)
    wishId INT NOT NULL, FOREIGN KEY (wishId) references wishes(wishId),

    # Potential user reserving wish for gifting (should not allow user owning list?)
    reserverId INT,      FOREIGN KEY (reserverId) references users(userId) ON DELETE SET NULL
);
