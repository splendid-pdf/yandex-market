create table types
(
    id          bigint not null
        primary key,
    name        varchar(255),
    external_id uuid
);

create table type_characteristics
(
    id         bigint not null
        primary key,
    name       varchar(255),
    value_type varchar(255),
    type_id    bigint
        references types
);

create table products
(
    id                  bigint  not null
        primary key,
    external_id         uuid,
    name                varchar(255),
    description         varchar(255),
    is_visible          boolean not null,
    is_archived         boolean not null,
    is_deleted          boolean not null,
    rating              double precision,
    seller_external_id  uuid,
    tax_type            varchar(255),
    price               bigint,
    article_from_seller varchar(255),
    count               bigint,
    type_id             bigint
        references types,
    creation_date       timestamp,
    brand               varchar(255)
);

create table product_images
(
    id         bigint not null,
    url        varchar(255),
    is_main    boolean,
    product_id bigint
        references products
);

insert into types(id, external_id, name)
values (1, '301c5370-be41-421e-9b15-f1e80a7079f9', 'спальня');

INSERT INTO type_characteristics (id, name, value_type, type_id)
VALUES (209, 'Ширина, см', 'DOUBLE', 1),
       (210, 'Глубина, см', 'DOUBLE', 1),
       (211, 'Высота, см', 'DOUBLE', 1),
       (212, 'Вес товара, кг', 'DOUBLE', 1),
       (213, 'Цвет', 'TEXT', 1),
       (214, 'Материал фасада', 'TEXT', 1),
       (215, 'Покрытие фасада', 'TEXT', 1),
       (216, 'Стиль дизайна', 'TEXT', 1),
       (217, 'Расположение', 'TEXT', 1),
       (218, 'Количество полок', 'LONG', 1),
       (219, 'Количество ящиков', 'LONG', 1),
       (220, 'Страна изготовитель', 'TEXT', 1),
       (221, 'Форма поставки', 'TEXT', 1),
       (222, 'Гарантийный срок', 'TEXT', 1);

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