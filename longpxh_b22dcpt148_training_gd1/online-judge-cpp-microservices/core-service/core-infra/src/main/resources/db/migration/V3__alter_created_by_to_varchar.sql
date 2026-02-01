-- Change created_by column type from BIGINT to VARCHAR(100) to store username instead of user ID
ALTER TABLE problems ALTER COLUMN created_by TYPE VARCHAR(100);
