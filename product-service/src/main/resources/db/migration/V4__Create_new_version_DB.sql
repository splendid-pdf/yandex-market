create sequence categories_sequence start with 1 increment by 1;
create sequence category_characteristics_sequence start with 1 increment by 1;
create sequence product_characteristics_sequence start with 1 increment by 1;
create sequence product_images_sequence start with 1 increment by 1;
create sequence product_special_prices_sequence start with 1 increment by 1;
create sequence products_sequence start with 1 increment by 1;
create sequence types_sequence start with 1 increment by 1;

create table categories
(
    id bigint                   not null,
    external_id                 uuid,
    name                        varchar(255),
    parent_category_external_id uuid,
    primary key (id)
);

create table category_characteristics
(
    id                          bigint not null,
    name                        varchar(255),
    value_type                  varchar(255),
    category_id                 bigint,
    primary key (id)
);

create table product_characteristics
(
    id bigint not null,
    name                        varchar(255),
    value                       varchar(255),
    value_type                  varchar(255),
    product_id                  bigint,
    primary key (id)
);

create table product_images
(
    id                          bigint not null,
    url                         varchar(255),
    product_id bigint,
    primary key (id)
);

create table product_special_prices
(
    id                          bigint not null,
    discounted_price            bigint,
    special_price_from_date     timestamp(6),
    special_price_to_date       timestamp(6),
    product_id                  bigint,
    primary key (id)
);

create table product_type
(
    type_id                     bigint not null,
    product_id                  bigint not null,
    primary key (product_id, type_id)
);

create table products
(
    id                          bigint not null,
    article_from_seller         varchar(255),
    article_number              varchar(255),
    count                       bigint,
    description                 varchar(255),
    height                      float(53),
    length                      float(53),
    weight                      float(53),
    width                       float(53),
    external_id                 uuid,
    is_deleted                  boolean,
    is_visible                  boolean,
    manufacturer_external_id    uuid,
    name                        varchar(255),
    price                       bigint,
    rating                      float(53),
    seller_external_id          uuid,
    tax_type                    varchar(255),
    category_id                 bigint,
    primary key (id)
);

create table types
(
    id                          bigint not null,
    external_id                 uuid,
    name                        varchar(255),
    primary key (id)
);

alter table if exists category_characteristics
    add constraint category_chrctFK foreign key (category_id) references categories;

alter table if exists product_characteristics
    add constraint product_chrctFK foreign key (product_id) references products;

alter table if exists product_images
    add constraint product_imgFK foreign key (product_id) references products;

alter table if exists product_special_prices
    add constraint product_spFK foreign key (product_id) references products;

alter table if exists product_type
    add constraint product_typeFK foreign key (product_id) references products;

alter table if exists product_type
    add constraint type_productFK foreign key (type_id) references types;

alter table if exists products
    add constraint categoryFK foreign key (category_id) references categories;