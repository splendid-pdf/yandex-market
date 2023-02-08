/*CREATE TABLE favorites(
    id SERIAL PRIMARY KEY,
    external_id uuid,
    productId uuid,
    userId uuid,
    additionTimestamp timestamp
);*/

INSERT INTO favorites(id,
                external_id,
                product_id,
                user_id,
                addition_timestamp)
VALUES (1,'5728bd51-996c-4ddf-a97d-57855203220d',
        '5728bd51-996c-4ddf-a97d-57355203720d',
        '5728bd51-996c-4ddf-a97d-57855203450d',
        '2023-01-13T10:14:33');