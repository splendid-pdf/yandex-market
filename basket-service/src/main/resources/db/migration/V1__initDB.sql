create sequence "baskets_sequence" start with 1 increment by 1;

create sequence "items_sequence" start with 1 increment by 1;

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

alter table if exists basket_items
    add constraint FKnx44qatthch57p2bgo873qfxu
        foreign key (basket_id)
            references baskets;

alter table if exists basket_items
    add constraint FKcrmfijy91sf3vd5h7f26a2agk
        foreign key (item_id)
            references items;

-- insert into baskets(id, external_id, user_id)
-- values (2, '83d85ba2-195d-4205-bc70-260199855347', '6a2e63a7-a8b7-4a5e-9422-6a16ee963e8d'),
--        (3, '79eacacf-3b23-4e6e-8b17-aacbdad4ba99', 'ef3d5941-2ead-442c-ab45-113d464591d5');
--
-- insert into items (id, external_id)
-- values (1, 'f34c4cd3-6fe7-4d3e-b82c-f5d044e46091'),
--        (2, '459f48f3-c6eb-4931-90d6-0d492658a223'),
--        (3, '75ce7550-3657-4811-a431-32bf05d4ef21'),
--        (4, '2b3e16c0-6479-483b-a3ae-6c25f44a4df2'),
--        (5, '02583e02-a22b-4df9-8827-ffb45f2c2914');
--
-- insert into basket_items(item_id, basket_id, item_count)
-- values (1, 2, 10),
--        (2, 2, 3),
--        (3, 2, 5),
--        (4, 2, 6),
--        (5, 2, 4),
--        (1, 3, 10),
--        (2, 3, 3),
--        (3, 3, 5),
--        (4, 3, 6),
--        (5, 3, 4);