create sequence "review_id_seq" start with 1 increment by 1;

create table reviews
(
    id                 bigint       not null,
    advantages         varchar(255),
    commentary         varchar(255),
    disadvantages      varchar(255),
    external_id        uuid         not null,
    product_id         uuid         not null,
    review_type        varchar(255),
    rating             integer      not null,
    creation_timestamp timestamp(6) not null,
    update_timestamp   timestamp(6),
    user_id            uuid         not null,
    primary key (id)
);

create table review_photo_ids
(
    review_id bigint not null REFERENCES reviews (id),
    photo_id  varchar(255)
);