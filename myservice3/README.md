# Spring Web Service accessing SQL Server DB / MySql Using JPA, Nested Entity One-to-Many
    https://spring.io/guides/gs/accessing-data-jpa/
    https://spring.io/guides/gs/accessing-data-mysql/
    https://stackoverflow.com/questions/52623524/json-return-nested-arrays-instead-of-objects-spring-boot-jpa-mysql-rest
    https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
	https://thepracticaldeveloper.com/produce-and-consume-json-messages-with-spring-boot-amqp/#introduction

    ; about map entity and DTO
    https://stackoverflow.com/questions/48231012/java-modelmapper-map-date-to-string-format-field
    https://www.programcreek.com/java-api-examples/?api=org.modelmapper.Converter
    https://stackoverflow.com/questions/51631692/modelmapper-and-localdate-spring-boot
    
    ; about database initialization
    https://www.javaskool.com/database-initialization-in-spring-boot/

## Compile/Run Commands in Bash command line
./mvnw clean package
java -jar target/myservice3-0.0.1-SNAPSHOT.jar --server.port=8080

; compile unit test, but not run unit test
mvn clean package -DskipTests

; To skip compiling the tests
mvn clean package -Dmaven.test.skip=true

## Plugin and Configuration

; if to make changes to the database  (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update

### pom.xml and application.properties for accessing SQL Server DB
		<dependency>
			<groupId>com.microsoft.sqlserver</groupId>
			<artifactId>mssql-jdbc</artifactId>
			<scope>runtime</scope>
		</dependency>

spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.datasource.url=${SQLSERVER_DB_TESTDB_URI:jdbc:sqlserver://localhost:1433;databaseName=testdb}
spring.datasource.username=sa
spring.datasource.password=V4321abcd!


#### Database privileges
mysql> revoke all on db_example.* from 'springuser'@'%';
mysql> grant select, insert, delete, update on db_example.* to 'springuser'@'%';
 
### pom.xml and application.properties for accessing MySql DB

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>

spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.url=${SQLSERVER_DB_TESTDB_URI:jdbc:mysql://localhost:3306/testdb}
spring.datasource.username=root
spring.datasource.password=root

## SQL to Clean Database

USE [testdb]
GO
SELECT Name From Sys.tables;

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[employees]') AND type in (N'U'))
DROP TABLE [dbo].[employees]

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ADDRESSES]') AND type in (N'U'))
DROP TABLE [dbo].[ADDRESSES]

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[USERS]') AND type in (N'U'))
DROP TABLE [dbo].[USERS]

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ORDER_ITEMS]') AND type in (N'U'))
DROP TABLE [dbo].[ORDER_ITEMS]

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ORDERS]') AND type in (N'U'))
DROP TABLE [dbo].[ORDERS]
GO

SELECT *
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME='users';

SELECT * FROM [users];
SELECT * FROM addresses;
SELECT * FROM orders;
SELECT * FROM order_items;
SELECT * FROM employees;

## cURL commands

curl http://localhost:8080/
curl http://localhost:8080/users

curl http://localhost:8080/users/1
curl http://localhost:8080/users/get?email=morgan@gmail.com -v

curl -X POST "http://localhost:8080/users/add?name=Jane1&email=jfu1@a.com" -v

curl http://localhost:8080/employees
curl http://localhost:8080/employees/1
curl -X POST -H "Content-Type:application/json" -d "{ \"firstName\": \"Tom\", \"lastName\":\"Mc\", \"title\": \"Sales Rep\", \"birthDate\": \"1972-01-23\", \"city\": \"Markham\", \"reportsTo\": 2 }" http://localhost:8080/employees -v
curl -X POST -H "Content-Type:application/json" -d "{ \"firstName\": \"Tom\", \"lastName\":\"Mc\", \"title\": \"Sales Rep\", \"birthDate\": \"1972-01-23\", \"hireDate\": \"2010-05-16\", \"city\": \"Markham\", \"reportsTo\": 2 }" http://localhost:8080/employees -v

curl -v -X PUT -H "Content-Type:application/json" -d "{ \"firstName\": \"Jack\", \"title\": \"RD VP\", \"reportsTo\": 1 }" http://localhost:8080/employees/632
curl -v -X DELETE localhost:8080/employees/632

curl "http://localhost:8080/myservice3-rabbitmq/employee?id=125&firstName=Kane&lastName=Mc3" -v
