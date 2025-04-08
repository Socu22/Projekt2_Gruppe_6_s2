
USE WishDatabase;

INSERT INTO users (username, password) VALUES
    ('Simon', 'rød'),
    ('Lucas', 'blå'),
    ('Emil', 'hvid'),
    ('Alexander', 'brun');

INSERT INTO listHolders (userId)
SELECT userId FROM users WHERE username = 'Simon';

INSERT INTO wishes (title, description, price) VALUES
    ('Lemonade', 'A cool refreshing drink, squeezed from lemons.', 21),
    ('Flamingo', 'A majestic pink-feathered bird with webbed feet, very long limbs, and a crooked beak.', 450),
    ('Doge Plushie','',10),
    ('Spicy Meme Sauce','',5),
    ('T-shirt','',15),
    ('Doge','',10),
    ('Doge','',10),
    ('Doge','',10),
    ('Doge','',10),
    ('Doge','',10),
    ('Doge','',10),
    ('Doge','',10),
    ('Doge','',10),
    ('Doge','',10),
    ('Doge','',10);

INSERT INTO wishLists (listId, wishId)
SELECT listId, wishId FROM listHolders, wishes WHERE userId = 1 OR title = 'Lemonade';