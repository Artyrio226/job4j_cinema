create table IF NOT EXISTS genres
(
    id   serial primary key,
    name varchar unique not null
);
