create table if not exists pharmacists(
    pharmacist_id BIGINT primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    pharmacist_email VARCHAR(30) not null,
    pharmacist_password VARCHAR(100) not null
);

select * from pharmacists p;