
USE WishDatabase;

INSERT INTO users (username, password) VALUES
    ('Simon', 'rød'),
    ('Lucas', 'blå'),
    ('Emil', 'hvid'),
    ('Alexander', 'brun');

INSERT INTO listHolders (userId)
SELECT userId FROM users WHERE username = 'Simon';

INSERT INTO listHolders (userId)
SELECT userId FROM users WHERE username = 'Lucas';

INSERT INTO listHolders (userId)
SELECT userId FROM users WHERE username = 'Alexander';

INSERT INTO listHolders (userId)
SELECT userId FROM users WHERE username = 'Simon';

INSERT INTO wishes (title, description, price) VALUES
    ('Lemonade', 'A cool refreshing drink, squeezed from lemons.', 21),
    ('Flamingo', 'A majestic pink-feathered bird with webbed feet, very long limbs, and a crooked beak.', 450),
    ('Doge Plushie','Because who doesn''t want to cuddle with the most iconic internet dog?',10),
    ('Spicy Meme Sauce','For when your memes need that extra kick.',5),
    ('Yeet Cannon','A toy cannon that shoots foam balls; often marketed as a fun, meme-inspired item.',30),
    ('Nyan Cat Toaster ','This is a fun, licensed toaster that toasts the Nyan Cat image onto your bread!',50),
    ('Fire Extinguisher','Because sometimes, this is fine, but also... maybe not.',35),
    ('Gucci Fidget Spinner','A cheap, meme-ified version of the fidget spinner; not a real Gucci item, of course!',15),
    ('Pepe Action Figure','For when you want to remember the good ol'' meme days.',20),
    ('SpongeBob Encyclopedia','A quirky, fun book filled with SpongeBob memes and references.',20),
    ('Boyfriend Cardboard Cutout','Perfect for spicing up a party or the lonely bachelorette.)',20),
    ('Big Chungus Plush','The ultimate internet chonk.',20),
    ('How Do You Do, Fellow Kids? Skateboard','A meme board usually designed for collectors or skaters who love memes.',80),
    ('Surprised Pikachu Pillow','A popular meme pillow with the Pikachu face; very plush and soft.',20),
    ('Grumpy Cat Coffee Mug','A classic meme coffee mug that’s very affordable.',10),
    ('Shrek Figurine','A life-sized figure, often seen in specialized meme or pop culture stores',200),
    ('Inflatable Dinosaur Costume','Transform into a giant inflatable dinosaur! Perfect for spontaneous dance parties, street parades, or confusing your friends.',50),
    ('Unicorn Meat (Canned)','Just when you thought you’d seen everything… here’s canned unicorn meat.',100),
    ('Rainbow-Shooting Umbrella','Beat the rainy blues by opening up an umbrella that shoots rainbows from the tips when you walk! It’s like magic for your morning commute.',30),
    ('Giant Inflatable Beach Ball Chair','Beat the rainy blues by opening up an umbrella that shoots rainbows from the tips when you walk! It’s like magic for your morning commute.',30),
    ('Cactus Shaped Desk Lamp','Bring a little desert charm to your workspace with a cactus-shaped desk lamp. Bonus points for the whimsical glow it provides.',15),
    ('Edible Cookie Dough Scented Candle','A candle that smells like warm, gooey cookie dough—without the calories. Sweeten your room and your mood without baking a thing!',5);

INSERT INTO wishLists (listId, wishId)
SELECT listId, wishId FROM listHolders, wishes WHERE listId = 1 AND wishId < 10;

INSERT INTO wishLists (listId, wishId)
SELECT listId, wishId FROM listHolders, wishes WHERE userId = 2 AND wishId > 6 AND wishId < 18;

INSERT INTO wishLists (listId, wishId)
SELECT listId, wishId FROM listHolders, wishes WHERE userId = 4 AND wishId > 14;
