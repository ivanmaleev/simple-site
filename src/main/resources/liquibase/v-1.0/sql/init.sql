INSERT INTO simple.users (id, username, password, active)
    VALUES (1, 'admin', 'admin', true);

INSERT INTO simple.user_roles (user_id, roles)
    VALUES (1, 0), (1, 1);

ALTER SEQUENCE simple.users_id_seq RESTART WITH 2;

CREATE extension IF NOT EXISTS pgcrypto;

UPDATE simple.users SET password = crypt(password, gen_salt('bf', 8));
