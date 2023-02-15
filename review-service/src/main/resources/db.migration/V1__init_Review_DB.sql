create table reviews
(
    id            bigint  not null,
    advantages    varchar(255),
    commentary    varchar(255),
    disadvantages varchar(255),
    external_id   uuid,
    product_id    uuid,
    review_type   varchar(255),
    score         integer not null,
    timestamp     timestamp(6),
    user_id       uuid,
    primary key (id)
);

create table review_photo_ids
(
    review_id bigint not null REFERENCES reviews (id),
    photo_id  varchar(255)
);