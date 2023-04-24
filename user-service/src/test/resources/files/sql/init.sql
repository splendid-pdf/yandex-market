create sequence "user_sequence" start with 5 increment by 1;

create table public.users
(
    id               bigint primary key     not null,
    external_id      uuid                   not null,
    first_name       character varying(100),
    last_name        character varying(100),
    phone            character varying(20),
    email            character varying(255) not null,
    login            character varying(255),
    password         character varying(255) not null,
    sex              character varying(15),
    city             character varying(255),
    delivery_address character varying(255),
    photo_id         character varying(255),
    is_deleted       boolean                not null,
    created_at       timestamp(6) without time zone,
    modified_at      timestamp(6) without time zone,
    role             character varying(20)  not null default 'USER'
);

INSERT INTO users (id, external_id, first_name, last_name, phone, email, login, password, sex, city, delivery_address,
                   photo_id, is_deleted, created_at, modified_at, role)
VALUES (1, 'f34c4cd3-6fe7-4d3e-b82c-f5d044e46091', 'John', 'Doe', '+79216437711', 'test@mail.ru', 'test', 'password',
        'MALE', 'Moscow', 'Tverskaya str, 34', 'id12', false, '2023-01-18 11:52:38.59412',
        '2023-01-18 11:52:38.59412', 'USER'),
       (2, 'c24c4cd3-6fe7-4d3e-b82c-f5d044e46058', 'Johny', 'Admin', '+79226344111', 'admin@mail.ru', 'admin', 'password',
        'MALE', 'Moscow', 'Tverskaya str, 34', 'id13', false, '2023-01-18 11:52:38.59412',
        '2023-01-18 11:52:38.59412', 'ADMIN')