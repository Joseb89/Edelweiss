create table if not exists doctors (
    doctor_id BIGINT primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    doctor_email VARCHAR(30) not null,
    doctor_password VARCHAR(100) not null,
    phone_number BIGINT not null,
    practice VARCHAR(25) not null
);

select * from doctors d;