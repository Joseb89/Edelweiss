create table if not exists users (
    user_id serial primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    user_email VARCHAR(30) not null,
    user_password VARCHAR(100) not null,
    user_role VARCHAR(11) not null
);