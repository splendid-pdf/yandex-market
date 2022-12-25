-- TODO: ДОДЕЛАТЬ
-- надо согласовать по каким полям ставить not null
create table shop_systems
(
    id            bigint       not null primary key,
    external_id   uuid         not null,
    is_disabled   boolean,
    is_test       boolean,
    city          varchar(255),
    country       varchar(255),
    house_number  varchar(5),
    latitude      double precision,
    longitude     double precision,
    office_number varchar(5),
    postcode      varchar(10),
    region        varchar(128),
    street        varchar(128),
    logo_url      varchar(512),
    name          varchar(255) not null unique,
    rating        real,
    email         varchar(128),
    number        varchar(14),
    token         varchar(64)
);
