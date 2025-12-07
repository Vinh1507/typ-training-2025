INSERT INTO roles (name, description, created_at, updated_at)
VALUES ('ROLE_ADMIN', 'Administrator role', NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (name, description, created_at, updated_at)
VALUES ('ROLE_USER', 'User role', NOW(), NOW())
ON CONFLICT (name) DO NOTHING;
