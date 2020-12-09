-- create the databases
--CREATE DATABASE IF NOT EXISTS projectone;

-- create the users for each database
--CREATE USER 'projectoneuser'@'%' IDENTIFIED BY 'V4321abcd!';
--GRANT CREATE, ALTER, INDEX, LOCK TABLES, REFERENCES, UPDATE, DELETE, DROP, SELECT, INSERT ON `projectone`.* TO 'projectoneuser'@'%';

-- to permit connect from out of container
update mysql.user set host='%' where user='root';
--create user 'user1'@'%' identified by 'V4321abcd!';
grant all privileges on *.* to 'user1'@'%' with grant option;

FLUSH PRIVILEGES;