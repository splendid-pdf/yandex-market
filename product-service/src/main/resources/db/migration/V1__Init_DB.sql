create sequence "product_characteristics_sequence" start with 1 increment by 1;
create sequence "product_images_sequence" start with 1 increment by 1;
create sequence "products_sequence" start with 1 increment by 1;
create sequence "product_special_prices_sequence" start with 1 increment by 1;
create sequence "rooms_sequence" start with 1 increment by 1;
create sequence "types_sequence" start with 1 increment by 1;
create sequence "type_characteristics_sequence" start with 1 increment by 1;

create table types
(
    id          bigint not null,
    name        varchar(255),
    external_id uuid,
    primary key (id)
);

create table products
(
    id                  bigint  not null,
    external_id         uuid,
    article_number      uuid,
    name                varchar(255),
    description         varchar(255),
    is_visible          boolean not null,
    is_archived         boolean not null,
    is_deleted          boolean not null,
    rating              float,
    seller_external_id  uuid,
    tax_type            varchar(255),
    price               BIGINT,
    article_from_seller varchar(255),
    count               BIGINT,
    type_id             bigint REFERENCES types (id),
    creation_date       timestamp,
    primary key (id)
);

create table product_characteristics
(
    id          bigint not null,
    external_id uuid,
    name        varchar(255),
    value       varchar(255),
    product_id  bigint REFERENCES products (id),
    value_type  varchar(255),
    primary key (id)
);

create table product_special_prices
(
    id                      bigint not null,
    external_id             uuid,
    special_price_from_date timestamp,
    special_price_to_date   timestamp,
    special_price           bigint,
    product_id              bigint REFERENCES products (id),
    primary key (id)
);

create table rooms
(
    id          bigint not null,
    external_id uuid,
    name        varchar(255),
    primary key (id)
);

CREATE TABLE product_images
(
    id         bigint not null,
    url        varchar(255),
    is_main    boolean,
    product_id bigint REFERENCES products (id)
);

create table room_type
(
    room_id INT NOT NULL REFERENCES rooms (id),
    type_id INT NOT NULL REFERENCES types (id)
);

create table type_characteristics
(
    id         bigint,
    name       varchar(255),
    value_type varchar(255),
    type_id    bigint REFERENCES type_characteristics (id),
    primary key (id)
);