create table if not exists prescriptions(
    prescription_id serial primary key,
    doctor_first_name VARCHAR(20) not null,
    doctor_last_name VARCHAR(30) not null,
    prescription_name VARCHAR(20) not null,
    prescription_dosage SMALLINT not null,
    prescription_status VARCHAR(8) not null
);