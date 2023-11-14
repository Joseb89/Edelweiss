create table if not exists doctors (
    doctor_id serial primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    doctor_email VARCHAR(30) not null,
    doctor_password VARCHAR(100) not null,
    phone_number BIGINT not null,
    practice VARCHAR(25) not null,
    role VARCHAR(9) not null
);

create table if not exists pharmacists (
    pharmacist_id serial primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    pharmacist_email VARCHAR(30) not null,
    pharmacist_password VARCHAR(100) not null,
    role VARCHAR(10) not null
);

create table if not exists patients (
    patient_id serial primary key,
    first_name VARCHAR(20) not null,
    last_name VARCHAR(30) not null,
    patient_email VARCHAR(30) not null,
    patient_password VARCHAR(100) not null,
    phone_number BIGINT not null,
    primary_doctor VARCHAR(50),
    blood_type VARCHAR(3) not null,
    role VARCHAR(7) not null
);

create table if not exists address (
    patient_id BIGINT primary key,
    street_address VARCHAR(40) not null,
    city VARCHAR(20) not null,
    state VARCHAR(2) not null,
    zipcode INTEGER not null,
    CONSTRAINT fk_patient
        FOREIGN KEY(patient_id)
            REFERENCES patients(patient_id)
);

create table if not exists prescriptions (
    prescription_id serial primary key,
    doctor_first_name VARCHAR(20) not null,
    doctor_last_name VARCHAR(30) not null,
    prescription_name VARCHAR(20) not null,
    prescription_dosage SMALLINT not null,
    prescription_status VARCHAR(8) not null
);

create table if not exists appointments (
    appointment_id serial primary key,
    doctor_first_name VARCHAR(20) not null,
    doctor_last_name VARCHAR(30) not null,
    patient_first_name VARCHAR(20) not null,
    patient_last_name VARCHAR(30) not null,
    appointment_date date not null,
    appointment_time time not null
);