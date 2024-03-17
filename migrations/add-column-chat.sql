--liquibase formatted sql

--changeset LalisaST:1.1
alter table chat
add create_at timestamp with time zone default current_timestamp not null
