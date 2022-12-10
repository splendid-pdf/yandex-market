create table branches
(
    id             bigint       not null primary key,
    email          varchar(128),
    hotline_phone  varchar(14),
    service_phone  varchar(14),
    external_id    uuid         not null,
    city           varchar(128),
    country        varchar(64),
    house_number   integer,
    latitude       double precision,
    longitude      double precision,
    office_number  integer,
    postcode       varchar(8),
    region         varchar(128),
    street         varchar(128),
    name           varchar(255) not null,
    ogrn           varchar(20),
    pickup         boolean,
    token          varchar(64),
    shop_system_id bigint
        constraint fk_shop_system
            references shop_systems
);