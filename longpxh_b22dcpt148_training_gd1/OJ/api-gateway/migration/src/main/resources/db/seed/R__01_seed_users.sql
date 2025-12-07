INSERT INTO users (username, email, full_name, password_hash, created_at, updated_at)
VALUES ('sadmin', 'sadmin@gmail.com', 'Super Admin', '$2a$10$WT04moRAv4fF/ivoeScy.ud8Y4njvuq5em/YeqgHKVA6GGFRiVbDK', NOW(), NOW())
ON CONFLICT (username) DO UPDATE SET password_hash = EXCLUDED.password_hash;

INSERT INTO users (username, email, full_name, password_hash, created_at, updated_at)
VALUES ('longpxh', 'longpxh@gmail.com', 'Phạm Xuân Hoàng Long', '$2a$10$89Zsk09QGCKLhE5NOuq6O.r42wvRrAX27DH6H2R5c2G.JdWZ3ilYS', NOW(), NOW())
ON CONFLICT (username) DO UPDATE SET password_hash = EXCLUDED.password_hash;
