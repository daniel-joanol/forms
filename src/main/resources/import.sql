INSERT INTO forms.role (id, name) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');
INSERT INTO forms.group (id, name, max_users, total_users) VALUES (1, 'GROUP_000001', 1, 1);
INSERT INTO forms."_user" (id, first_name, last_name, username, password, is_enabled, group_id) VALUES (1, 'Daniel', 'Joanol', 'joanoldaniel@gmail.com', '$2a$10$woY/S6/faTHGqMgrJnsmZeQ6UWTew8FHtLV4cqmCyzZ/jBlTFFi36', true, 1);
INSERT INTO forms.user_roles (user_id, role_id) VALUES (1, 1), (1, 2);
INSERT INTO forms.group_users (group_id, user_id) VALUES (1, 1);