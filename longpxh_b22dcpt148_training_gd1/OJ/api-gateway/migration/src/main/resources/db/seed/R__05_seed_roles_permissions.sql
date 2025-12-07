-- Admin Role (1) has all permissions (1, 2, 3, 4)
INSERT INTO roles_permissions (role_id, permission_id) SELECT 1, 1 WHERE NOT EXISTS (SELECT 1 FROM roles_permissions WHERE role_id = 1 AND permission_id = 1);
INSERT INTO roles_permissions (role_id, permission_id) SELECT 1, 2 WHERE NOT EXISTS (SELECT 1 FROM roles_permissions WHERE role_id = 1 AND permission_id = 2);
INSERT INTO roles_permissions (role_id, permission_id) SELECT 1, 3 WHERE NOT EXISTS (SELECT 1 FROM roles_permissions WHERE role_id = 1 AND permission_id = 3);
INSERT INTO roles_permissions (role_id, permission_id) SELECT 1, 4 WHERE NOT EXISTS (SELECT 1 FROM roles_permissions WHERE role_id = 1 AND permission_id = 4);

-- User Role (2) has read permission (2)
INSERT INTO roles_permissions (role_id, permission_id) SELECT 2, 2 WHERE NOT EXISTS (SELECT 1 FROM roles_permissions WHERE role_id = 2 AND permission_id = 2);
