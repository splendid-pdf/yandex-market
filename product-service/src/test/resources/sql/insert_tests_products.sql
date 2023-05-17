create table types
(
    id          bigint not null     primary key,
    name        varchar(255),
    external_id uuid
);

create table rooms
(
    id          bigint not null     primary key,
    external_id uuid,
    name        varchar(255)
);

create table type_characteristics
(
    id         bigint not null      primary key,
    name       varchar(255),
    value_type varchar(255),
    type_id    bigint               references types,
    group_characteristic varchar(255)
);

create table products
(
    id                  bigint  not null    primary key,
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
    type_id             bigint              references types,
    creation_date       timestamp,
    brand               varchar(255)
);

create table product_images
(
    id         bigint not null,
    url        varchar(255),
    is_main    boolean,
    product_id bigint           references products
);

create table product_special_prices
(
    id          bigint not null     primary key,
    external_id uuid,
    from_date   timestamp,
    to_date     timestamp,
    price       bigint,
    product_id  bigint              references products
);

insert into types(id, external_id, name)
values (1, '301c5370-be41-421e-9b15-f1e80a7079f9', 'спальня');

INSERT INTO type_characteristics (id, name, value_type, type_id, group_characteristic)
VALUES (209, 'Ширина, см', 'DOUBLE', 1, 'Габариты'),
       (210, 'Глубина, см', 'DOUBLE', 1, 'Габариты'),
       (211, 'Высота, см', 'DOUBLE', 1, 'Габариты'),
       (212, 'Вес товара, кг', 'DOUBLE', 1, 'Габариты'),
       (213, 'Цвет', 'TEXT', 1, 'Габариты'),
       (214, 'Материал фасада', 'TEXT', 1, 'Габариты'),
       (215, 'Покрытие фасада', 'TEXT', 1, 'Габариты'),
       (216, 'Стиль дизайна', 'TEXT', 1, 'Габариты'),
       (217, 'Расположение', 'TEXT', 1, 'Габариты'),
       (218, 'Количество полок', 'LONG', 1, 'Габариты'),
       (219, 'Количество ящиков', 'LONG', 1, 'Габариты'),
       (220, 'Страна изготовитель', 'TEXT', 1, 'Габариты'),
       (221, 'Форма поставки', 'TEXT', 1, 'Габариты'),
       (222, 'Гарантийный срок', 'TEXT', 1, 'Габариты');

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
       (111, 'https://random.imagecdn.app/111/111', 11, false),
       (1111, 'https://random.imagecdn.app/123/123', 11, false),
       (12, 'https://random.imagecdn.app/200/200', 12, true),
       (13, 'https://random.imagecdn.app/300/300', 13, true),
       (14, 'https://random.imagecdn.app/400/400', 14, true);

insert into product_special_prices (id, external_id, from_date, to_date, price, product_id)
values (100,
        'ee6d7da1-32ac-4d24-84ec-adc5c0ca9552',
        '2028-06-13T10:14:33',
        '2028-07-13T10:14:33',
        3456,
        11);

create table product_characteristics
(
    id          bigint not null primary key,
    external_id uuid,
    name        varchar(255),
    value       varchar(255),
    product_id  bigint        references products,
    value_type  varchar(255),
    group_characteristic varchar(255)
);

INSERT INTO product_characteristics (id, external_id, name, value, product_id, value_type, group_characteristic)
VALUES (100, 'f3bb4ee9-c624-472b-8e4d-669dc863267d', 'Ширина, см', 123, 11, 'DOUBLE', 'Габариты'),
       (101, 'a969bc99-324b-4e7e-aa72-1d7f197948e5', 'Глубина, см', 12.3, 11, 'DOUBLE', 'Габариты'),
       (102, '81de4b86-ea7d-484e-8217-53f7d36691a3', 'Высота, см', 34.5, 11, 'DOUBLE', 'Габариты'),
       (103, '90082405-f0e1-41b2-a34b-5c5717fc1f3d', 'Вес товара, кг', 456.7, 11, 'DOUBLE', 'Габариты'),
       (104, 'a782e3ac-6165-425b-b61c-ce93654f9aa3', 'Цвет', 'красный', 11, 'TEXT', 'Габариты'),
       (105, '1c884d4c-7bc6-4d54-8b21-d83015568e70', 'Материал фасада', 'стальной', 11, 'TEXT', 'Габариты'),
       (106, '13f2dd2f-8255-48f3-9991-c70e28d79a6d', 'Покрытие фасада', 'стальной', 11, 'TEXT', 'Габариты'),
       (107, '91b42d5d-12c8-4f60-a5dc-b34334b0c610', 'Стиль дизайна', 'минимализм', 11, 'TEXT', 'Габариты'),
       (108, '3919c626-87de-4070-b72e-b12be29cd7d3', 'Расположение', 'по центру', 11, 'TEXT', 'Габариты'),
       (109, '0e1ab7c4-5816-4efe-8052-6c199b0fb566', 'Количество полок', 4, 11, 'LONG', 'Габариты'),
       (110, '1c92165b-13ec-43c9-b095-57c7b41f3cb8', 'Количество ящиков', 3, 11, 'LONG', 'Габариты'),
       (111, '56805755-04dd-4873-9b52-290423fa8761', 'Страна изготовитель', 'Гонконг', 11, 'TEXT', 'Габариты'),
       (112, '39d1a737-8a4f-40dd-b217-14b561c3109f', 'Форма поставки', 'доставка', 11, 'TEXT', 'Габариты'),
       (113, 'c55cd11a-2ff8-483e-aa6e-a7d50529d16c', 'Гарантийный срок', '6 м.', 11, 'TEXT', 'Габариты');
