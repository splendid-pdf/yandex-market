create sequence "contact-generator" start with 1 increment by 50;

create sequence "user-generator" start with 1 increment by 1;

create table contacts (id bigint not null,
                       value varchar(255),
                       type smallint,
                       user_id bigint,
                       primary key (id));

create table users (id bigint not null,
                    external_id uuid not null,
                    first_name varchar(255) not null,
                    middle_name varchar(255),
                    last_name varchar(255) not null,
                    phone varchar(255),
                    email varchar(255) not null unique,
                    login varchar(255) not null unique,
                    password varchar(255) not null,
                    birthday date,
                    sex varchar(255),
                    country varchar(255),
                    region varchar(255),
                    city varchar(255),
                    postcode varchar(255),
                    street varchar(255),
                    house_number varchar(255),
                    apartment_number varchar(255),
                    email_to_send varchar(255),
                    is_allowed_to_store_response_to_my_reviews boolean default FALSE,
                    is_allowed_to_send_discounts_and_promotions_mailing_lists boolean default FALSE,
                    is_allowed_to_send_popular_articles boolean default FALSE,
                    photo_id varchar(255),
                    is_deleted boolean not null,
                    created_at timestamp(6),
                    modified_at timestamp(6),
                    primary key (id));

alter table if exists contacts
    add constraint FK_UserContact foreign key (user_id) references users