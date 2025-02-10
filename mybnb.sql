
drop table if exists reservations;
drop table if exists acc_occupancy;


create table acc_occupancy (
    acc_id varchar(10) not null,
    vacancy int ,
    constraint pk_acc_occupancy primary key (acc_id)
);

create table reservations (
    resv_id varchar(8) not null,
    name_ varchar(128) not null,
    email varchar(128) not null,
    acc_id varchar(10) not null,
    arrival_date date,
    duration int,
    constraint pk_reservations primary key (resv_id),
    constraint fk_reservations foreign key (acc_id) references acc_occupancy(acc_id) ON DELETE CASCADE
);