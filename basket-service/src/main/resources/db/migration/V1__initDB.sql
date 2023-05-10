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
    item_id         bigint REFERENCES items(id),
    basket_id       bigint REFERENCES baskets(id),
    item_count      integer,
    primary key (basket_id, item_id)
);