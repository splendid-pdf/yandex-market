/*CREATE TABLE favoriteItem(
    id SERIAL PRIMARY KEY,
    external_id uuid,
    productId uuid,
    userId uuid,
    additionTimestamp timestamp
);*/

INSERT INTO favoriteItem(id,
                external_id,
                product_id,
                user_id,
                added_at)
VALUES (1,'5728bd51-996c-4ddf-a97d-57855203220d',
        '5728bd51-996c-4ddf-a97d-57355203720d',
        '5728bd51-996c-4ddf-a97d-57855203450d',
        '2023-01-13T10:14:33');