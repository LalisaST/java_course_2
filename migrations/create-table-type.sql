--liquibase formatted sql

--changeset LalisaST:2.0
create table type
(
    type varchar(63) primary key
);

insert into type (type)
values ('GITHUB'),
       ('STACKOVERFLOW');

alter table link
    add column type varchar(63) not null;

alter table link
    add constraint fk_link_type foreign key (type) references type (type) on update cascade;


alter table only link
    add column commit_count int default null;

alter table only link
    add column answer_count int default null;

alter table only link
    add column comment_count int default null;
