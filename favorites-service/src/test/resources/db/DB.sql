/*INSERT INTO users(id, external_id) VALUES(1, '5728bd51-996c-4ddf-a97d-57855203450d');*/

INSERT INTO favoriteItem(id,
                      external_id,
                      product_id,
                      user_id,
                      added_at)
VALUES (1,'5728bd51-996c-4ddf-a97d-57855203220d',
        '5728bd51-996c-4ddf-a97d-57355203720d',
        '5728bd51-996c-4ddf-a97d-57855203450d',
        '2023-01-13T10:14:33');

/*alter table if exists favoriteItem add constraint user_id_FK foreign key(user_id) references users;*/