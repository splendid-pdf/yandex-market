
create sequence favorite_product_seq start with 1 increment by 1
create sequence favorites_seller_seq start with 1 increment by 1

create table if not exists favorite_products (
    id bigint not null,
    external_id uuid not null,
    user_id uuid not null,
    added_at timestamp(6) not null,
    primary key (id)
)

create table if not exists favorites_sellers (
    id bigint not null,
    external_id uuid not null,
    user_id uuid not null,
    added_at timestamp(6) not null,
    primary key (id)
)