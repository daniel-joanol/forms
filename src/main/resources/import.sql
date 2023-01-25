INSERT INTO forms.role (id, name) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN'), (3, 'ROLE_GROUP_000001');
INSERT INTO forms."_user" (id, first_name, last_name, username, password, is_enabled) VALUES (1, 'Daniel', 'Joanol', 'joanoldaniel@gmail.com', '$2a$10$woY/S6/faTHGqMgrJnsmZeQ6UWTew8FHtLV4cqmCyzZ/jBlTFFi36', true);
INSERT INTO forms.user_roles (user_id, role_id) VALUES (1, 1), (1, 2), (1, 3);