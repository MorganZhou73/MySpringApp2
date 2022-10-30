# Getting Started
. https://www.baeldung.com/spring-boot-data-sql-and-schema-sql
 set spring.jpa.hibernate.ddl-auto=none will ensure that script-based initialization is performed using schema.sql and data.sql directly.
 
 
$ mvn clean package
$ java -jar target/*.jar

## Switch H2 DB and MySQL
    1. in pom.xml
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>
		
		-- OR 
		
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		
	2. copy  application-H2.properties or application-MySql.properties to application.properties 
	
		     
## Configure and Access H2 Console
  https://howtodoinjava.com/spring-boot2/h2-database-example/
  
  http://localhost:8080/h2
  
## Init DB: load schema.sql and data.xml
    https://www.baeldung.com/spring-boot-data-sql-and-schema-sql

  . for H2, if entity has field of @GeneratedValue
      need    @GeneratedValue(strategy = GenerationType.IDENTITY)  
      or @GeneratedValue(strategy = GenerationType.SEQUENCE) 
      and make sure that you have index or sequence on database level
      
## API
spring.application.name=service4
$server.servlet.context-path=/api/${spring.application.name}

    curl "http://localhost:8080/v1/users/uid01"
    
    curl "http://localhost:8080/v1/users?page=1&limit=10"
	
	curl --request DELETE 'http://localhost:8080/v1/users/uid02'
    
	curl --location --request PUT 'http://localhost:8080/v1/users/uid01' \
	--header 'Content-Type: application/json' \
	--data-raw '{
		"firstName":"Morgan2",
		"lastName":"Zhou",
		"password":"123456"
	}'
    
### Log file

	<appender name="STDOUT"
		class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
		</encoder>
	</appender>
	
    => get like
14:43:12.855 [http-nio-8080-exec-2] INFO  c.u.m.controller.UsersController - UsersController:  list userEntity
	    
	<appender name="FILE" class="ch.qos.logback.core.FileAppender">
		<file>${logFile}</file>
		<encoder>
		    <pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n </pattern>
		</encoder>
	</appender>
	=>
2022-10-27 14:43:12,855 INFO [http-nio-8080-exec-2] c.u.m.c.UsersController [UsersController.java:30] UsersController:  list userEntity
     