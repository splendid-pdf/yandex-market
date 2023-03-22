create sequence "user-sequence" start with 1 increment by 1;

create table users
(
    id                                                        bigint       not null,
    external_id                                               uuid         not null,
    first_name                                                varchar(255) not null,
    middle_name                                               varchar(255),
    last_name                                                 varchar(255) not null,
    phone                                                     varchar(255),
    email                                                     varchar(255) not null unique,
    login                                                     varchar(255) not null unique,
    password                                                  varchar(255) not null,
    birthday                                                  date,
    sex                                                       varchar(255),
    country                                                   varchar(255),
    region                                                    varchar(255),
    city                                                      varchar(255),
    postcode                                                  varchar(255),
    street                                                    varchar(255),
    house_number                                              varchar(255),
    apartment_number                                          varchar(255),
    is_allowed_to_receive_on_address                          boolean default FALSE,
    is_allowed_to_send_discounts_and_promotions_mailing_lists boolean default FALSE,
    is_allowed_to_send_popular_articles                       boolean default FALSE,
    photo_url                                                 varchar(255),
    is_deleted                                                boolean      not null,
    created_at                                                timestamp(6),
    modified_at                                               timestamp(6),
    primary key (id)
);