create table users (
                       id bigint not null constraint users_pkey primary key,
                       passport_id varchar(1024),
                       first_name varchar(1024),
                       email varchar(1024) NOT NULL UNIQUE,
                       last_name varchar(1024),
                       phone varchar(1024),
                       balance int default 0
);
CREATE SEQUENCE user_seq START 1;