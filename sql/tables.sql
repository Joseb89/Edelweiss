create table if not exists users (
    user_id serial primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    user_email VARCHAR(30) not null,
    user_password VARCHAR(100) not null,
    user_role VARCHAR(11) not null
);

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

create table if not exists doctors (
    doctor_id BIGINT primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    doctor_email VARCHAR(30) not null,
    doctor_password VARCHAR(100) not null,
    phone_number BIGINT not null,
    practice VARCHAR(25) not null
);

create table if not exists pharmacists (
    pharmacist_id BIGINT primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    pharmacist_email VARCHAR(30) not null,
    pharmacist_password VARCHAR(100) not null
);

create table if not exists prescriptions (
    prescription_id serial primary key,
    doctor_first_name VARCHAR(20) not null,
    doctor_last_name VARCHAR(30) not null,
    prescription_name VARCHAR(20) not null,
    prescription_dosage SMALLINT not null,
    prescription_status VARCHAR(8) not null
);