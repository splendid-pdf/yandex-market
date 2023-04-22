insert into types(id, external_id, name)
values (1, '301c5370-be41-421e-9b15-f1e80a7079f9', 'спальня');

insert into products (id, external_id, name, description, is_visible, is_archived, is_deleted, rating,
                      seller_external_id, tax_type, price, article_from_seller, count, type_id, creation_date, brand)
values (11, '0a751cc5-4325-444e-ac1d-df48c678d7b1', 'Комод 1',
        'Классный комод 1', true, false, false, 5,
        'cb041d31-a345-4d80-971a-70c49cbc5c28', 'MAX', 1000,
        'komod1super1', 100, 1, '2023-03-22T20:49:49.279+04:00',
        'Супер комоды'),
       (12, '0a751cc5-4325-444e-ac1d-df48c678d7b2', 'Комод 2',
        'Классный комод 2', true, false, false, 5,
        'cb041d31-a345-4d80-971a-70c49cbc5c28', 'MAX', 2000,
        'komod2super1', 200, 1, '2023-03-22T20:49:49.279+04:00',
        'Супер комоды'),
       (13, '0a751cc5-4325-444e-ac1d-df48c678d7b3', 'Комод 3',
        'Классный комод 3', false, true, false, 5,
        'cb041d31-a345-4d80-971a-70c49cbc5c28', 'MAX', 3000,
        'komod3super1', 300, 1, '2023-03-22T20:49:49.279+04:00',
        'Супер комоды'),
       (14, '0a751cc5-4325-444e-ac1d-df48c678d7b4', 'Комод 4',
        'Классный комод 4', false, false, false, 5,
        'cb041d31-a345-4d80-971a-70c49cbc5c28', 'MAX', 4000,
        'komod4super1', 400, 1, '2023-03-22T20:49:49.279+04:00',
        'Супер комоды');

INSERT INTO product_images (id, url, product_id, is_main)
VALUES (11, 'https://random.imagecdn.app/100/100', 11, true),
       (12, 'https://random.imagecdn.app/200/200', 12, true),
       (13, 'https://random.imagecdn.app/300/300', 13, true),
       (14, 'https://random.imagecdn.app/400/400', 14, true);