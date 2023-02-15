INSERT INTO reviews (id, advantages, commentary, disadvantages, external_id, product_id, review_type, score, timestamp,
                     user_id)
VALUES (1, 'быстро жарятся блинчики', 'сковордка как сковородка', 'медленно жарятся оладушки',
        '9728bd51-996c-4ddf-a97d-57855203720d', '5428bd51-996c-4ddf-a97d-57855203720d', 'PRODUCT_REVIEW', 4,
        '2023-01-13T10:14:33', 'ed39e6e1-bf4a-4e77-b29a-69cd82bfc516'),
       (2, 'быстро жарятся блинчики', 'сковордка как сковородка', 'медленно жарятся оладушки',
        '2328bd51-996c-4ddf-a97d-57855203720d', '5428bd51-996c-4ddf-a97d-57855203720d', 'PRODUCT_REVIEW', 5,
        '2023-01-13T10:14:33', 'ed39e6e1-bf4a-4e77-b29a-69cd82bfc516');

INSERT INTO review_photo_ids (review_id, photo_id)
VALUES (1, '123'),
       (1, '12'),
       (2, '22'),
       (2, '222');