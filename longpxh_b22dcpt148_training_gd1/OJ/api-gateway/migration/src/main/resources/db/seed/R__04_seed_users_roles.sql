-- Admin (id 1) has ROLE_ADMIN (id 1)
INSERT INTO users_roles (user_id, role_id)
SELECT 1, 1
WHERE NOT EXISTS (SELECT 1 FROM users_roles WHERE user_id = 1 AND role_id = 1);

-- User (id 2) has ROLE_USER (id 2)
INSERT INTO users_roles (user_id, role_id)
SELECT 2, 2
WHERE NOT EXISTS (SELECT 1 FROM users_roles WHERE user_id = 2 AND role_id = 2);
