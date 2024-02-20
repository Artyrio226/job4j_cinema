create table IF NOT EXISTS files
(
    id   serial primary key,
    name varchar not null,
    path varchar not null unique
);
