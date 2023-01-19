INSERT INTO forms.role (id, name) VALUES (1, 'USER'), (2, 'ADMIN');
INSERT INTO forms."_user" (id, first_name, last_name, email, password, is_enabled) VALUES (1, 'Daniel', 'Joanol', 'joanoldaniel@gmail.com', '$2a$10$woY/S6/faTHGqMgrJnsmZeQ6UWTew8FHtLV4cqmCyzZ/jBlTFFi36', true);
INSERT INTO forms.user_roles (user_id, role_id) VALUES (1, 1), (1, 2);