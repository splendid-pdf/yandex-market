
create sequence favorites_sequence start with 1 increment by 1
create sequence favorite_product_seq start with 1 increment by 1
create sequence favorites_seller_seq start with 1 increment by 1

create table favorites (
   id bigint not null PRIMARY KEY,
   user_id uuid not null,
   external_id uuid not null,
)

create table favorite_products (
    id bigint not null PRIMARY KEY,
    external_id uuid not null,
    favorite_item_id bigint not null,
    added_at timestamp(6) not null,
)

create table favorites_sellers (
   id bigint not null PRIMARY KEY,
   external_id uuid not null,
   favorite_item_id bigint not null,
   added_at timestamp(6) not null,
)

alter table if exists favorites
    add constraint favorites_fk unique (external_id)

alter table if exists favorite_products
    add constraint favorite_products_fk
    foreign key (favorite_item_id)
    references favorites

alter table if exists favorites_sellers
    add constraint favorite_sellers_fk
    foreign key (favorite_item_id)
    references favorites