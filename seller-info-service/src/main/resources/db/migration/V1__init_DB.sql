CREATE SEQUENCE "sellers_sequence" start with 1 increment by 1;

CREATE TABLE sellers
(
    id                bigint       not null,
    external_id       uuid         not null,
    first_name        varchar(50),
    last_name         varchar(100),
    email             varchar(255) not null unique,
    password          varchar(255) not null,
    legal_address     varchar(255),
    company_name      varchar(255),
    role              varchar(20)  not null default 'SELLER',
    image_id         varchar(255),
    business_model    varchar(255),
    itn               varchar(255),
    psrn              varchar(255),
    bic               varchar(255),
    payment_account   varchar(255),
    corporate_account varchar(255),
    is_deleted        boolean,
    created_at        timestamp,
    modified_at       timestamp
);