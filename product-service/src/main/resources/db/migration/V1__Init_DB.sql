create sequence "category-generator" start with 1 increment by 1;
create sequence "characteristic-generator" start with 1 increment by 1;
create sequence "product_price-generator" start with 1 increment by 1;
create sequence "products-generator" start with 1 increment by 1;

create table category
(
    id             bigint  not null,
    external_id    uuid,
    name           varchar(255),
    description    varchar(255),
    parent_id      bigint,
    sorting_factor bigint,
    image_url      varchar(255),
    is_visible     boolean not null,
    is_deleted     boolean not null,
    primary key (id)
);

create table characteristics
(
    id         bigint not null,
    name       varchar(255),
    value      varchar(255),
    product_id bigint,
    primary key (id)
);

create table product_price
(
    id                      bigint    not null,
    external_id             uuid,
    product_id              uuid,
    branch_id               uuid,
    shop_system_id          uuid,
    price                   float(53) not null,
    discounted_price        float(53) not null,
    special_price_from_date timestamp(6),
    special_price_to_date   timestamp(6),
    primary key (id)
);

create table products
(
    id             bigint    not null,
    external_id    uuid,
    article_number bigint,
    name           varchar(255),
    description    varchar(255),
    product_type   varchar(255),
    manufacturer   varchar(255),
    weight         float(53) not null,
    image_url      varchar(255),
    length         varchar(255),
    width          varchar(255),
    sorting_factor bigint,
    rating         float(53),
    is_visible     boolean   not null,
    is_deleted     boolean   not null,
    primary key (id)
);

alter table if exists characteristics
    add constraint productFK foreign key (product_id) references products