-- liquibase formatted sql

-- changeset msinyavskaya:1
create index student_name_index on student (name);

--changeset msinyavskaya:2
create index faculty_nc_index on faculty (name, color);