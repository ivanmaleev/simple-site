delete from simple.user_roles;
delete from simple.users;

insert into simple.users(id, username, password, active) values
(1, 'admin', '$2a$08$YVxJ.2C8WhHq8XjkVrOF3um4hUvyl9fk9TRVRySDxgDd8Z6OLi4LW', true),
(2, 'user', '$2a$08$c8SmsFMsaq9wnPL978jTou1L5V0fjdiMN7PyieGFdBJIffqLNPo0y', true);

insert into simple.user_roles(user_id, roles) values
(1, 1), (1, 2),
(2, 2);