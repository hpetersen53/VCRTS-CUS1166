create table vehicleOwner (ownerId int not null primary key, fname varchar(20), lname varchar(20), email varchar(50), password varchar(20), licenseNum int);

create table vehicle (vin int not null primary key, make varchar(20), model varchar(20), color varchar(20), year int, licensePlate varchar(8), residency double, ownerId int not null, foreign key(ownerId) references vehicleOwner(ownerId));

create table client (clientId int not null primary key, fname varchar(20), lname varchar(20), email varchar(50), password varchar(20), licenseNum int);

create table job (jobId int not null primary key, clientId int not null, redundancy int, duration int, payout double, title varchar(50), deadline date, attachedFile varchar(50), foreign key(clientId) references client(clientId));