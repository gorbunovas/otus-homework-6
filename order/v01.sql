create table orders (
                        id bigint not null constraint users_pkey primary key,
                        price int,
                        status varchar(1024),
                        user_id Long
);
CREATE SEQUENCE order_seq START 1;