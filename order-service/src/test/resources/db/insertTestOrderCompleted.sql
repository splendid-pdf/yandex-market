INSERT INTO orders (id, external_id, user_id, price, order_status, payment_type, paid, payment_date_time,
                    creation_timestamp, country, region, city, post_code, street, house_number, office_number,
                    receiver_name, receiver_phone, receiver_email, delivery_date, delivery_start, delivery_end,
                    delivery_cost, delivery_method)
VALUES (5, '37678201-f3c8-4d5c-a628-2344eef50c54', 'cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04', 1000, 'COMPLETED', 'CASH',
        'true', '2023-05-13T10:14:33', '2023-05-13T10:14:33', 'Russia', 'Len obl', 'Piter', '123456', 'street',
        '23', '01', 'Ivan', '+79103658847', 'test@mail.ru', '2023-05-14', '14:00:00', '20:00:00', 2,
        'HOME_DELIVERY');

INSERT INTO ordered_products (id, amount, article_from_seller, description, name, photo_url, price, product_id, seller_id, order_id)
VALUES (1, 1, '37678201-f3c8-4d5c-a628-2344eef50c54', 'description', 'name', 'photo.ru', 90, 'cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04',
        'cd8ae5aa-ebea-4922-b3c2-8ba8a296ef05', 5);