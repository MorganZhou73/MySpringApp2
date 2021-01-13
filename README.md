# Demo for Spring RESTfull Web Service accessing MongoDB/SQL Server/RabbitMQ
	docker-compose to initiate MongoDB/SqlServer
	
## Guides
	
	\Docker
		Demo docker-compose to initiate MongoDB/SqlServer/MySql/Kafka in Docker
		
	\myservice1: Spring accessing MySql, 
		send/receive message from Kafka
		
	\myservice2: Spring RESTfull Web Service accessing MongoDB
		to demo Spring MVC, WebSecurityConfigurerAdapter
		receive message from RabbitMQ, and save to MongoDB
	
	\myservice3: Spring Web Service accessing SQL Server DB
		send message to RabbitMQ
	
	\SimpleAngularJS:
		simple web to test AngularJS
		to start server:
			spring run app.groovy -- --server.port=8080
		
	\SimpleJQuery:
		simple web to test jQuery 
		to start server:
			spring run app.groovy -- --server.port=9101

	\SimpleNodeJS:
		simple web to test NodeJS 
		to start server:
			node myfirst.js
			; then you can access http://localhost:8080/  -- the port is specified in the .js
		
	\angularapp1: 
		Spring Boot with Angular 

	\myreactapp2: React JS project. generated by 
		npx create-react-app myreactapp2

-------------------------------------------------------------------------
## How to Run on Docker
 
 - Approch 1 for myservice2/myservice3: 
  	use docker-compose to build myservice2/myservice3 from Docker automatically, this needs to pull maven.
  
	docker-compose -f docker-compose-v1.yml up -d

  - Approch 2 for myservice2/myservice3: 
	build myservice2/myservice3 locally at first, so no need to pull maven:
	
	1. compile myservice2 to generate target/*.jar from Command window
		cd ./myservice2
		mvn clean package -Dmaven.test.skip=true
			-- or 
		mvn clean package -DskipTests
		cd ..
	
	2. compile myservice3 to generate target/*.jar from Command window
		cd ./myservice3
		mvn clean package -Dmaven.test.skip=true
		cd ..

	3. load the compose to container and run (from either Command window or PowerShell )
		docker-compose -f docker-compose.yml up -d

	then, we can test 
		myservice2 : http://localhost:8081/
		myservice3 : http://localhost:8080/
	
	; delete all the relative containers, but the docker images remains
	docker-compose -f docker-compose.yml down
	
-------------------------------------------------------------------------
## Clean the relative containers and images in docker

docker stop myservice2
docker rm myservice2

docker stop myservice3
docker rm myservice3

docker stop mongodb_container
docker rm mongodb_container

docker stop rabbitmq
docker rm rabbitmq

docker stop sql-server-db
docker rm sql-server-db

docker image rm myservice2:tag-1.0.0
docker image rm myservice3:tag-1.0.0
docker image rm sql-testdb:tag-1.0.0

; to find network of a container
docker inspect sql-server-db -f "{{json .NetworkSettings.Networks }}"

-------------------------------------------------------------------------
## Compile commands

; compile and run unit test
mvn clean package

; compile unit test, but not run unit test
mvn clean package -DskipTests

; To skip compiling the tests
mvn clean package -Dmaven.test.skip=true

; Run all the unit test classes.
mvn test

; Run a single test class.
mvn -Dtest=TestApp1 test

; Run multiple test classes.
mvn -Dtest=TestApp1,TestApp2 test

; Run a single test method from a test class.
mvn -Dtest=TestApp1#methodname test

-------------------------------------------------------------------------
## Test RabbitMQ on Docker Seperately
	docker pull rabbitmq:3-management-alpine
	docker run --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management-alpine
	
	docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management-alpine

-------------------------------------------------------------------------
## Test MongoDB on Docker Seperately

	cd ./docker

	docker pull mongo:latest
	
	; launch the image as container
	docker-compose -f docker-compose-mongo.yml up -d
	docker ps -a
	
	; Login to the container by using container names from PowerShell
	docker exec -it mongodb_container bash
	
	; login to the container from Git Bash
	winpty docker exec -it mongodb_container bash
	
	; then could login to mongoDB: the db user name should be different with the root user name for Docker test
	mongo -u "root" -p "V4321abcd!" --authenticationDatabase "admin"
	mongo -u "user1" -p "V4321abcd!" --authenticationDatabase "Northwind"
	
	use admin
	show users;
	show dbs;

	use Northwind
	show users;
	; should get like { "_id" : "Northwind.user1", ...} ; otherwise, may fail for the mongo Docker test
	
	show collections 
	db.person.find()
	db.person.save({"firstName":"Joe", "lastName":"Zhou"})	

	; to remove the container from docker
	docker stop mongodb_container
	docker rm mongodb_container
	
	docker-compose -f docker-compose-mongo.yml down

	docker logs mongodb_container | grep error
	db = db.getSiblingDB("Northwind");
	
	; view javascript file in mongo container
	cat /docker-entrypoint-initdb.d/init-mongo.js
	
	;Execute a JavaScript file:
	load("/docker-entrypoint-initdb.d/init-mongo.js")
	
	; we can also use Robo 3T to verify the DB
-------------------------------------------------------------------------
## Both postgres and mysql images already support using environment variable 
   to create an initial database when the image is run and the container is 
   created (POSTGRES_DB: "mydb" or MYSQL_DATABASE: "mydb")
   
   For MS SqlServer, cannot initialize DB from docker-compose directly

-------------------------------------------------------------------------
## Test SqlServer on Docker Seperately

	cd ./docker
	docker build -t sql-testdb:tag-1.0.0 -f Dockerfile-mssql .
	
	; launch the image as container
	docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=V4321abcd!' -e 'MSSQL_DB=TestDB' -e 'MSSQL_USER=user1' -e 'MSSQL_PASSWORD=V4321abcd!' -p 1433:1433 --name sql-server-db -d sql-testdb:tag-1.0.0
	
	; or run from docker-compose
	docker-compose -f docker-compose-mssql.yml up -d

	; Login to the container by using container names from PowerShell
	docker exec -it sql-server-db bash
	
	; login to the container from Git Bash
	winpty docker exec -it sql-server-db bash
	
	/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P 'V4321abcd!'
	/opt/mssql-tools/bin/sqlcmd -S localhost -U user1 -P 'V4321abcd!'
		1> SELECT @@VERSION
		2> GO
		
		1> select name from sys.databases;
		2> go

	; access from outside of the Docker, e.g. Command window	
	OSQL -S localhost -U sa -P "V4321abcd!" -Q "USE [master]; SELECT @@Version"
	OSQL -S localhost,1433 -U sa -P "V4321abcd!" -Q "USE [master]; SELECT @@Version"
	
	; to remove the container and image	from docker
	docker stop sql-server-db
	docker rm sql-server-db
	docker image rm sql-testdb:tag-1.0.0
	
	docker ps -a
	docker image ls
	
	; we can also use Azure Data Studio to verify the DB

-------------------------------------------------------------------------
## Make images for myservice2 / myservice3

	1. make image for myservice2:tag-1.0.0
		cd ./myservice2
		mvn clean package
		docker build -t myservice3:tag-1.0.0 .
	
	2. make image for myservice3:tag-1.0.0
		cd ./myservice3
		mvn clean package
		docker build -t myservice3:tag-1.0.0 .

### Push to Docker hub https://hub.docker.com/
		cd ./myservice1
		docker build -t zmg9046/myservice1:tag-1.0.0 .
		cd ../myservice2
		docker build -t zmg9046/myservice2:tag-1.0.0 .
		cd ../myservice3
		docker build -t zmg9046/myservice3:tag-1.0.0 .

		docker login
		docker push zmg9046/myservice1:tag-1.0.0
		docker push zmg9046/myservice2:tag-1.0.0
		docker push zmg9046/myservice3:tag-1.0.0

	; to remove the image from hub.docker via command line
	curl -X DELETE -u "$user:$pass" https://hub.docker.com/repository/docker/zmg9046/test

	
-------------------------------------------------------------------------
## Managing MySql on Docker
	https://dev.mysql.com/doc/refman/8.0/en/docker-mysql-getting-started.html
	https://hub.docker.com/_/mysql
	
	docker pull mysql/mysql-server:latest
	docker pull mysql:8.0.20
		; -- or download the .tar, then
	docker load -i mysql-enterprise-server-version.tar

	; launch the image as container
	docker-compose -f docker-compose-mysql.yml up -d
		; --- or
	docker run --name=mysql1 -e MYSQL_ROOT_PASSWORD='V4321abcd!' -d mysql/mysql-server:latest
	docker run -p 3306:3306 --name=mysql1 -e MYSQL_ROOT_PASSWORD='V4321abcd!' -e MYSQL_DATABASE='MyDB' -e MYSQL_USER='user1' -e MYSQL_PASSWORD='V4321abcd!' -d mysql/mysql-server:latest
	
	docker run -p 3306:3306 --name=mysql1 -e MYSQL_ROOT_PASSWORD='V4321abcd!' -e MYSQL_DATABASE='MyDB' -e MYSQL_USER='user1' -e MYSQL_PASSWORD='V4321abcd!' -d mysql:8.0.20
	
	; Connecting to MySQL Server from within the Container in PowerShell window
	; while in GitBash window, need add winpty
	winpty docker exec -it mysql1 mysql --user=root --password='V4321abcd!'
	winpty docker exec -it mysql1 mysql --user=user1 --password='V4321abcd!'
 	
	docker logs mysql1
	
	; check the password of ROOT user if not set from environment from GitBash  
	docker logs mysql1 2>&1 | grep GENERATED
		-- [Entrypoint] GENERATED ROOT PASSWORD: *Id,yGuHj4Puh4vuw60lUf+OtAw
	
	; check docker container ip
	docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql1
	docker inspect mysql1 | grep "IPAddress"
	
	docker exec -it mysql1 bash	
	docker exec -it mysql1 mysql -uroot -p '*Id,yGuHj4Puh4vuw60lUf+OtAw'
	
	; change root password if not set MYSQL_ROOT_PASSWORD from docker run 
	ALTER USER 'root'@'localhost' IDENTIFIED BY 'V4321abcd!';
	
	SELECT VERSION(), CURRENT_DATE, user();
	SHOW DATABASES;
	select host, user from mysql.user;
	update mysql.user set host = '%' where user='root';
	
	use mysql;
	show tables;
	
	docker restart mysql1
	docker inspect mysql1
	docker port mysql1 3306
	
	docker stop mysql1
	docker rm mysql1
	docker start mysql1
   
	; we can use MySQL Workbench to check the DB
	https://dev.mysql.com/downloads/workbench/
-------------------------------------------------------------------------
## Managing Kafka on Docker
	https://docs.spring.io/spring-kafka/reference/html/#introduction
	https://rmoff.net/2018/08/02/kafka-listeners-explained/
	https://github.com/rmoff/kafka-listeners
	
	docker-compose -f docker-compose-kafka.yml up -d
	
	1. Producer Side
	docker exec -it kafka1 bash
	
	; To create a new topic named test
	kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic test
	 
	; To start a producer that publishes datastream from standard input to kafka
	kafka-console-producer.sh --broker-list localhost:9092 --topic test

	2. Consumer side
	; from another window : GitBash
	winpty docker exec -it kafka1 bash
	kafka-console-consumer.sh --bootstrap-server kafka1:29092 --topic test
	
	; listen a new topic from new window (use INTERNAL listener, default is localhost:9092)
	kafka-console-consumer.sh --bootstrap-server kafka1:29092 --topic message-topic
	kafka-console-consumer.sh --bootstrap-server kafka1:29092 --topic post-topic
	
	; press Ctrl+C to exit the listener
	
	3. from Producer Side window, input a message string; press Enter key,
	   then in Consumer side window, you will see the message.
	
	// Print out the topics
	kafka-topics.sh --bootstrap-server kafka1:29092 --list
	kafka-topics.sh --bootstrap-server :9092 --list
	
	// Describe topic t1
	kafka-topics.sh --bootstrap-server :9092 --describe --topic test

### Config of Kafka
	# There are three listeners: 
	# INTERNAL: for internal traffic on the Docker network
	# EXTERNAL: for traffic from the Docker-host machine (localhost)
	# DOMAIN1 for traffic from outside, reaching the Docker host on the DNS name 'dns1'
  kafka1:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
      - "29094:29094"
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENERS: INTERNAL://kafka1:29092,EXTERNAL://kafka1:9092,DOMAIN1://kafka1:29094
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka1:29092,EXTERNAL://localhost:9092,DOMAIN1://dns1:29094
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT,DOMAIN1:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL

		
	. LISTENERS are what interfaces Kafka binds to. ADVERTISED_LISTENERS are how clients can connect.
	. KAFKA_LISTENERS is a comma-separated list of listeners, and the host/ip and port to which Kafka binds to on which to listen. For more complex networking this might be an IP address associated with a given network interface on a machine. The default is 0.0.0.0, which means listening on all interfaces.
	. KAFKA_ADVERTISED_LISTENERS is a comma-separated list of listeners with their the host/ip and port. This is the metadata that's passed back to clients.
	. KAFKA_LISTENER_SECURITY_PROTOCOL_MAP defines key/value pairs for the security protocol to use, per listener name.	
-------------------------------------------------------------------------
## simulate Jekins pipeline steps for myservice1

	git clone https://github.com/MorganZhou73/MySpringApp2.git
	cd MySpringApp2\myservice1
	
	docker-compose -f docker-compose-v1.yml up -d
	
	mvn -Dmaven.test.failure.ignore=true clean package
	//mvn clean package -DskipTests
	//mvn test
	
	docker-compose -f docker-compose.yml up -d
	
	// docker build -t myservice1:tag-1.0.0 .
	// docker run --network net-mysql  -e "KAFKA_URI=kafka1:29092" -e "mysql-servername=mysql1" -e "mysql-db=MyDB" -e "spring_datasource_username=user1" -e "spring_datasource_password=V4321abcd!" -e "spring_datasource_url=jdbc:mysql://mysql1:3306/MyDB?useSSL=false&allowPublicKeyRetrieval=true" -p 8082:8082 --name myservice1 -d myservice1:tag-1.0.0

	
-------------------------------------------------------------------------
   	  