INSERT INTO roles (name, description) VALUES
    ('STUDENT', 'Student role'),
    ('TEACHER', 'Teacher role'),
    ('ADMIN', 'Administrator role')
ON CONFLICT (name) DO NOTHING;

INSERT INTO permissions (name, description) VALUES
    ('READ_USERS', 'Permission to read user information'),
    ('WRITE_USERS', 'Permission to create/update users'),
    ('DELETE_USERS', 'Permission to delete users'),
    ('MANAGE_ROLES', 'Permission to manage roles and permissions')
ON CONFLICT (name) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN'
ON CONFLICT DO NOTHING;
