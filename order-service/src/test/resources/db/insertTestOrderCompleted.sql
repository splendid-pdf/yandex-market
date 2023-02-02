INSERT INTO orders (id, external_id, user_id, price, order_status, payment_type, paid, payment_date_time,
                    creation_timestamp, country, region, city, post_code, street, house_number, office_number,
                    receiver_name, receiver_phone, receiver_email, delivery_date, delivery_start, delivery_end,
                    delivery_cost, delivery_method)
VALUES (5, '37678201-f3c8-4d5c-a628-2344eef50c54', 'cd8ae5aa-ebea-4922-b3c2-8ba8a296ef04', '1000.00', 'COMPLETED', 'CASH',
        'true', '2023-05-13T10:14:33', '2023-05-13T10:14:33', 'Russia', 'Len obl', 'Piter', '123456', 'street',
        '23', '01', 'Ivan', '+79103658847', 'test@mail.ru', '2023-05-14', '14:00:00', '20:00:00', '2.00',
        'HOME_DELIVERY');

INSERT INTO ordered_products(id, amount, branch_id, description, name, photo_url, price, product_id, shop_system_id,
                             order_id)
VALUES (1, 1, 'e0e738a2-a2e0-4e15-bd73-c39f9ef0ece0', 'мощный хороший', 'planshet',
        'photo.ru', 2000, '9b114634-56d7-4660-801f-b18b1806f137', '118cb1f0-cb45-4ec7-89c3-f253a73d0d03', 5),
       (2, 2, 'e0e738a2-a2e0-4e15-bd73-c39f9ef0ece0', 'мощный хороший', 'planshet',
        'photo.ru', 2000, '9b114634-56d7-4660-801f-b18b1806f137', '118cb1f0-cb45-4ec7-89c3-f253a73d0d04', 5);