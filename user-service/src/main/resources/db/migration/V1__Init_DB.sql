create sequence "user-sequence" start with 1 increment by 1;

create table users
(
    id               bigint       not null,
    external_id      uuid         not null,
    first_name       varchar(255) not null,
    last_name        varchar(255) not null,
    phone            varchar(255),
    email            varchar(255) not null unique,
    login            varchar(255) not null unique,
    password         varchar(255) not null,
    sex              varchar(255),
    city             varchar(255),
    delivery_address varchar(255),
    photo_url        varchar(255),
    is_deleted       boolean      not null,
    created_at       timestamp(6),
    modified_at      timestamp(6),
    primary key (id)
);