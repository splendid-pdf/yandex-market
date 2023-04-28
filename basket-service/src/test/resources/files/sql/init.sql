create table baskets
(
    id              bigint primary key      not null,
    external_id     uuid                    not null,
    user_id         uuid                    not null

);

create table items
(
    id              bigint primary key      not null,
    external_id     uuid                    not null
);

create table basket_items
(
    item_id         bigint          not null,
    basket_id       bigint          not null,
    item_count      integer,
    primary key (basket_id, item_id)
);

INSERT INTO baskets(id, external_id, user_id)
VALUES (1, '83d85ba2-195d-4205-bc70-260199855347', '6a2e63a7-a8b7-4a5e-9422-6a16ee963e8d');

INSERT INTO items (id, external_id)
VALUES (1, 'f34c4cd3-6fe7-4d3e-b82c-f5d044e46091'),
       (2, '459f48f3-c6eb-4931-90d6-0d492658a223'),
       (3, '75ce7550-3657-4811-a431-32bf05d4ef21'),
       (4, '2b3e16c0-6479-483b-a3ae-6c25f44a4df2'),
       (5, '02583e02-a22b-4df9-8827-ffb45f2c2914');

INSERT INTO basket_items(item_id, basket_id, item_count)
VALUES (1, 1, 10),
       (2, 1, 3),
       (3, 1, 5),
       (4, 1, 6),
       (5, 1, 4);