create table if not exists patients (
    patient_id BIGINT primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    patient_email VARCHAR(30) not null,
    patient_password VARCHAR(100) not null,
    phone_number BIGINT not null,
    street_address VARCHAR(40),
    city VARCHAR(20),
    state VARCHAR(2),
    zipcode INTEGER,
    primary_doctor VARCHAR(50),
    blood_type VARCHAR(3) not null
);

select * from patients p;

drop table if exists patients;