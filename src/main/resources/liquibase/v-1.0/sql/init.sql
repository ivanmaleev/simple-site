insert into simple.users (id, username, password, active)
    values (1, 'admin', 'admin', true);

insert into simple.user_roles (user_id, roles)
    values (1, 0), (1, 1);

create extension if not exists pgcrypto;

update simple.users set password = crypt(password, gen_salt('bf', 8));
