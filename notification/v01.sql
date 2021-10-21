create table notifications (
                               id bigint not null constraint users_pkey primary key,
                               title varchar(1024),
                               body varchar(1024),
                               email varchar(1024),
                               user_id bigint
);
CREATE SEQUENCE notification_seq START 1;