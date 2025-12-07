INSERT INTO permissions (name, description, created_at, updated_at) VALUES ('problem:create', 'Create problems', NOW(), NOW()) ON CONFLICT (name) DO NOTHING;
INSERT INTO permissions (name, description, created_at, updated_at) VALUES ('problem:read', 'Read problems', NOW(), NOW()) ON CONFLICT (name) DO NOTHING;
INSERT INTO permissions (name, description, created_at, updated_at) VALUES ('problem:update', 'Update problems', NOW(), NOW()) ON CONFLICT (name) DO NOTHING;
INSERT INTO permissions (name, description, created_at, updated_at) VALUES ('problem:delete', 'Delete problems', NOW(), NOW()) ON CONFLICT (name) DO NOTHING;
