CREATE TABLE IF NOT EXISTS users (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	phone VARCHAR(255) NULL,
	address VARCHAR(255) NULL,
	PRIMARY KEY (id),
	UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users (name, email, phone, address) VALUES
	('Alice Nguyen', 'alice@example.com', '0901000001', 'Ha Noi'),
	('Bob Tran', 'bob@example.com', '0901000002', 'Da Nang'),
	('Charlie Le', 'charlie@example.com', '0901000003', 'Ho Chi Minh')
ON DUPLICATE KEY UPDATE
	name = VALUES(name),
	phone = VALUES(phone),
	address = VALUES(address);
