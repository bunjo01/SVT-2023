INSERT INTO user (id, description, email, first_name, image_id, last_name, password, role, suspended, username, last_login)
VALUES (DEFAULT, 'admins desc', 'admin@gmail.com', 'admin', null, 'admin', '$2a$10$98InvpwYdz8/RU1Rf0bNOeya2XGucfa/c3Vavx5IPNJzDhCbjvuou', 1, 0, 'admin', null);

INSERT INTO user (id, description, email, first_name, image_id, last_name, password, role, suspended, username, last_login)
VALUES (DEFAULT, 'users desc', 'user@gmail.com', 'user', null, 'user', '$2a$10$xaZ3DcCCoCuLENmCKk6Uk.tPiwJeCLeKAynNFcL2xYK1ZLQ73n/nu', 0, 0, 'user', null);