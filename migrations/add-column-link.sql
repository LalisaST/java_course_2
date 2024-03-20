--liquibase formatted sql

--changeset LalisaST:1.2
alter table link
    add last_check timestamp with time zone default current_timestamp not null;

alter table link
    alter column last_update set default current_timestamp;
