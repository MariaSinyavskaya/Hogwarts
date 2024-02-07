create table car (
    id serial primary key,
    car_brand varchar(255) not null,
    car_model varchar(255) not null,
    cost money not null
);

create table person (
    id serial primary key,
    name varchar(255) not null,
    age integer check (age > 17),
    drivers_license boolean not null,
    car_id integer references car (id)
);

insert into car (id, car_brand, car_model, cost) values (
    1, 'Volkswagen', 'Tiguan', 3150000);

insert into person (id, name, age, drivers_license, car_id) values (
    1, 'Nikita', 19, 'yes', 1);

insert into person (id, name, age, drivers_license, car_id) values (
    2, 'Maria', 18, 'true', 1);