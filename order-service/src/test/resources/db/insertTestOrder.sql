INSERT INTO orders (id, external_id, user_id, price, order_status, payment_type, paid, payment_date_time,
                    creation_timestamp, country, region, city, post_code, street, house_number, office_number,
                    receiver_name, receiver_phone, receiver_email, delivery_date, delivery_start, delivery_end,
                    delivery_cost, delivery_method)
VALUES (1, '37678201-f3c8-4d5c-a628-2344eef50c54', '37678201-f3c8-4d5c-a628-2344eef50c54', 22.00, 'CREATED', 'CASH',
        true, '2023-05-13T10:14:33', '2023-05-13T10:14:33', 'Russia', 'Len obl', 'Piter', '123456', 'street',
        '23', '01', 'Ivan', '+79103658847', 'test@mail.ru', '2023-05-14', '14:00:00', '20:00:00', 2.00, 'HOME_DELIVERY')
