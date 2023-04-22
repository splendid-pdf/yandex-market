insert into types(id, external_id, name)
values (1, '301c5370-be41-421e-9b15-f1e80a7079f9', 'spalnya');

insert into products (id, external_id, name, description, is_visible, is_archived, is_deleted, rating,
                      seller_external_id, tax_type, price, article_from_seller, count, type_id, creation_date, brand)
values (1, '0a751cc5-4325-444e-ac1d-df48c678d7b1', 'asdf 1',
        'asdfadf1', true, false, false, 5,
        'cb041d31-a345-4d80-971a-70c49cbc5c28', 'MAX', 1000,
        'komod1super1', 100, 1, '2023-03-22T20:49:49.279+04:00',
        'sdfasdf'),
       (2, '0a751cc5-4325-444e-ac1d-df48c678d7b2', 'adsfadsf 2',
        'adf asdf 2', true, false, false, 5,
        'cb041d31-a345-4d80-971a-70c49cbc5c28', 'MAX', 2000,
        'komod2super1', 200, 1, '2023-03-22T20:49:49.279+04:00',
        'sdfasdf'),
       (3, '0a751cc5-4325-444e-ac1d-df48c678d7b3', 'asdfadsf 3',
        'sdfasdf 3', false, true, false, 5,
        'cb041d31-a345-4d80-971a-70c49cbc5c28', 'MAX', 3000,
        'komod3super1', 300, 1, '2023-03-22T20:49:49.279+04:00',
        'sdfasdf');

INSERT INTO product_images (id, url, product_id, is_main)
VALUES (1, 'https://random.imagecdn.app/100/100', 1, true),
       (2, 'https://random.imagecdn.app/200/200', 2, true),
       (3, 'https://random.imagecdn.app/300/300', 3, true);