create table orders
(
    id                 bigint       not null,
    external_id        uuid         not null,
    user_id            uuid         not null,
    price              float        not null,
    order_status       varchar(255) not null,
    ordered_products   varchar(255) not null,
    payment_type       varchar(255) not null,
    receipt_method     varchar(255) not null,
    paid               boolean      not null,
    payment_date_time  timestamp,
    creation_timestamp timestamp,
    country            varchar(255) not null,
    region             varchar(255) not null,
    city               varchar(255) not null,
    post_code          varchar(255) not null,
    street             varchar(255) not null,
    house_number       varchar(255) not null,
    office_number      varchar(255) not null,
    receiver_name      varchar(255) not null,
    receiver_phone     varchar(255) not null,
    receiver_email     varchar(255) not null,
    delivery_date      date         not null,
    delivery_start     time         not null,
    delivery_end       time         not null,
    delivery_cost      float        not null,
    delivery_method    varchar(255) not null,
    primary key (id)
);


create table ordered_products
(
    id             bigint       not null,
    product_id     uuid         not null,
    name           varchar(255) not null,
    price          float        not null,
    description    varchar(255) not null,
    photo_url      varchar(255) not null,
    branch_id      uuid         not null,
    shop_system_id uuid         not null,
    order_id       bigint REFERENCES orders (id),
    primary key (id)
);
