-- for test only, remove tables created by other projects
DROP TABLE IF EXISTS roles_authorities;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS password_reset_tokens;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT not null,
  USER_ID VARCHAR(30) NOT NULL,
  FIRST_NAME VARCHAR(50) NOT NULL,
  LAST_NAME VARCHAR(50) NOT NULL,
  EMAIL VARCHAR(100) NOT NULL,
  ENCRYPTED_PASSWORD VARCHAR(250) NULL,
  INDEX (id)
);

--create INDEX c_id on users(id);
