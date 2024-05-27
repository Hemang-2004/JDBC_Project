drop database hospital;
create database hospital;
use hospital;

CREATE TABLE patients(
    patient_name varchar(60) not null,
    dob date,
    gender char(1) not null,
    wardNumber int not null,
    Ward varchar(60),
    CONSTRAINT chk_gender CHECK (gender IN ('M', 'F')),
    patientId int primary key
);
create table doctors
(
    doctor_name varchar(60) not null,
    dob date,
    salary decimal(10, 2) not null,
    gender char(1),
    CONSTRAINT chk_gender_tc CHECK (gender IN ('M', 'F')),
    CONSTRAINT chk_salary CHECK (salary > 0),
    docId int primary key
    
);


create table medicine
(
    mid int primary key, 
    name varchar(100)
);

create table medication
(
    pid int,
    docid int,
    mid int,
    primary key(pid,mid,docid),
    CONSTRAINT fk_patient FOREIGN KEY (pid) REFERENCES patients(patientId)   ON DELETE CASCADE,
    CONSTRAINT fk_doctor FOREIGN KEY (docid) REFERENCES doctors(docId)  ON DELETE CASCADE,
    CONSTRAINT fk_medicine FOREIGN KEY (mid) REFERENCES medicine(mid)  ON DELETE CASCADE
);
create table room
(
    roomNumber int primary key,
    patId int,
    CONSTRAINT fk_patId FOREIGN KEY (patId) REFERENCES patients(patientId) ON DELETE SET NULL
);