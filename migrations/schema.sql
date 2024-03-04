--liquibase formatted sql

--changeset LalisaST:1
create table chat
(
    id bigint primary key
);

create table link
(
    id          bigserial primary key,
    url         varchar unique           not null,
    last_update timestamp with time zone not null
);

create table chat_link
(
    link_id bigint references link (id) on delete cascade,
    chat_id bigint references chat (id) on delete cascade,
    primary key (link_id, chat_id)
)
