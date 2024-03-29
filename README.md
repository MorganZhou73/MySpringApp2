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
		
	\myservice4: Spring Web Service accessing H2 In-memory DB 	
		switch between H2 and MySQL DB
		Postman collection : looping Post request demo
		
		\myservice4\ui: Angular UI to consume myservice4 API
	
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
		https://spring.io/guides/tutorials/spring-security-and-angular-js/
		https://github.com/spring-guides/tut-spring-security-and-angular-js/tree/main/basic

	\myreactapp2: React JS project. generated by 
		npx create-react-app myreactapp2
		
		$ yarn start
		or
		$ npm start
		
		; the browser will be started with http://localhost:3000/ 

-------------------------------------------------------------------------
## How to Run on Docker
 
  - compile all myservice1/myservice2/myservice3
	mvn clean package -DskipTests
 
  - Approch 1 for myservice2/myservice3: 
  	use docker-compose to build myservice2/myservice3 from Docker automatically, this needs to pull maven.
  
	docker-compose -f docker-compose-v1.yml up -d

  - Approch 2 for myservice2/myservice3: 
	build myservice2/myservice3 locally at first, so no need to pull maven:
	
	1. compile myservice2 to generate target/*.jar from Command window
		mvn clean package -f ./myservice2/pom.xml -Dmaven.test.skip=true
			-- or 
		mvn clean package -f ./myservice2/pom.xml -DskipTests
	
	2. compile myservice3 to generate target/*.jar from Command window
		mvn clean package -f ./myservice3/pom.xml -DskipTests

	3. load the compose to container and run (from either Command window or PowerShell )
		docker-compose -f docker-compose.yml up -d
		
		  -- or load and run myservice2/myservice3 only:
		docker-compose -f docker-compose-service2.yml up -d
		docker-compose -f docker-compose-service3.yml up -d

	then, we can test 
		myservice2 : http://localhost:9081/
		myservice3 : http://localhost:9080/
	
	; delete all the relative containers, but the docker images remains
	docker-compose -f docker-compose.yml down
	
  - for myservice1: 
	mvn clean package -f ./myservice1/pom.xml -DskipTests
	
	docker-compose -f ./myservice1/docker-compose.yml up -d
	   ;then go to http://localhost:8082/ which has links to send messages 
	   
	docker-compose -f ./myservice1/docker-compose.yml down
	
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
	
	kafka-console-producer.sh -broker-list localhost:9092 -topic message-topic
    kafka-console-consumer.sh --bootstrap-server kafka1:9092 --topic message-topic --from-beginning --max-messages 10
	
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
      - "29092:29092"
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENERS: INTERNAL://:9092,EXTERNAL://:29092,DOMAIN1://:29094
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka1:9092,EXTERNAL://localhost:29092,DOMAIN1://dns1:29094
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
## Deploy Helm chart to Kubernetes : 

cd ./charts

$ kubectl create ns hello

$ kubectl get ns
	NAME              STATUS   AGE
	default           Active   7d2h
	hello             Active   3s
	kube-node-lease   Active   7d2h
	kube-public       Active   7d2h
	kube-system       Active   7d2h

$ kubectl get ns

### deploy myservice2 (with MongoDB and RabbitMq) only
$ helm install myservice2 myservice2 --namespace hello

$ helm ls --namespace hello
	NAME            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART                       APP VERSION
	myservice2      hello           1               2021-04-24 14:37:48.1546629 -0400 EDT   deployed        myservice2app-0.1.0-SNAPSHOT

$ kubectl -n hello get all
	NAME                              READY   STATUS    RESTARTS   AGE
	pod/mongodb1-0                    2/2     Running   0          2m41s
	pod/myservice2-64847459f6-tlgbv   1/1     Running   2          2m41s
	pod/rabbitmq1-0                   1/1     Running   0          2m41s

	NAME                                   TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                                 AGE
	service/mongodb1                       ClusterIP   10.104.171.8    <none>        27017/TCP                               2m42s
	service/mongodb1-metrics               ClusterIP   10.107.27.167   <none>        9216/TCP                                2m42s
	service/myservice2                     ClusterIP   10.98.31.240    <none>        8081/TCP                                2m41s
	service/myservice2-rabbitmq-headless   ClusterIP   None            <none>        4369/TCP,5672/TCP,25672/TCP,15672/TCP   2m42s
	service/rabbitmq1                      ClusterIP   10.99.210.149   <none>        5672/TCP,4369/TCP,25672/TCP,15672/TCP   2m42s

	NAME                         READY   UP-TO-DATE   AVAILABLE   AGE
	deployment.apps/myservice2   1/1     1            1           2m41s

	NAME                                    DESIRED   CURRENT   READY   AGE
	replicaset.apps/myservice2-64847459f6   1         1         1       2m41s

	NAME                         READY   AGE
	statefulset.apps/mongodb1    1/1     2m41s
	statefulset.apps/rabbitmq1   1/1     2m41s

$ kubectl -n hello port-forward service/myservice2 9081:8081
	; then http://localhost:9081/ can be accessible
	; the MongoDB will be created only after the first access, e.g. http://localhost:9081/people
	
	; to verify the CrossOrigin only permits from port 8080 website:
	PS E:\SimpleAngularJS> spring run app.groovy -- --server.port=8080
	; http://localhost:8080/index.html : input DestUrl as http://localhost:9081/greeting?name=joe , press check
	; will get response as "The content is Good morning, joe!"
	
	PS E:\SimpleAngularJS> spring run app.groovy -- --server.port=8085
	; http://localhost:8085/index.html : input DestUrl as http://localhost:9081/greeting?name=joe , press check
	; will get no response 
	
$ kubectl -n hello port-forward service/mongodb1 27017:27017
	; then can use Robo 3T to connect localhost:27017
	
$ kubectl -n hello port-forward service/rabbitmq1 15672:15672
	; then, can access the RabbitMQ Management interface http://localhost:15672/ 
		username: user  ;  password: user1

PS E:\> kubectl -n hello exec -it service/mongodb1 -- bash
	> mongo admin --host "mongodb1" --authenticationDatabase admin -u root -p "V4321abcd!"
	> db.system.users.find()
	> show dbs;
	Northwind  0.000GB
	admin      0.000GB
	config     0.000GB
	local      0.000GB
	> use Northwind
	switched to db Northwind
	> show collections
	person
	> db.person.find();

PS E:\> kubectl -n hello exec -it service/rabbitmq1 -- bash

PS E:\Temp> kubectl -n hello logs service/myservice2 >myservice2-log1.txt

$ helm uninstall myservice2 --namespace hello

### Deploy myservice3 with (SqlServer DB, RabbitMq) only

$ helm install myservice3 myservice3 --namespace hello

$ kubectl -n hello port-forward service/myservice3 9080:8080

PS E:\> kubectl exec -it service/sql-server-db -- bash
	/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P "V4321abcd!"

	> SELECT @@VERSION;
	> GO
	> SELECT name FROM sys.databases;
	> GO

$ helm uninstall myservice3 --namespace hello

### Deploy myservice2 and myservice3 with MongoDB, SqlServer DB, RabbitMq

$ helm install service2n3 service2n3 --namespace hello

$ kubectl -n hello port-forward service/myservice2 9081:8081
$ kubectl -n hello port-forward service/myservice3 9080:8080

$ kubectl -n hello port-forward service/rabbitmq1 15672:15672
	
$ helm uninstall service2n3 --namespace hello

### Deploy myservice1 with mysql, Kafka and zookeeper

$ helm install myservice1 myservice1 --namespace hello

$ kubectl -n hello get all
	NAME                              READY   STATUS    RESTARTS   AGE
	pod/myservice1-67cc9f58db-gnmwq   1/1     Running   6          15m
	pod/myservice1-kafka-0            1/1     Running   2          15m
	pod/myservice1-zookeeper-0        1/1     Running   0          15m
	pod/mysql1-675ff8696c-s9lp7       1/1     Running   0          15m

	NAME                                    TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
	service/kafka1                          ClusterIP      10.98.89.253     <none>        9092/TCP                     15m
	service/myservice1                      ClusterIP      10.102.130.65    <none>        8082/TCP                     15m
	service/myservice1-kafka-headless       ClusterIP      None             <none>        9092/TCP,9093/TCP            15m
	service/myservice1-zookeeper            ClusterIP      10.103.123.106   <none>        2181/TCP,2888/TCP,3888/TCP   15m
	service/myservice1-zookeeper-headless   ClusterIP      None             <none>        2181/TCP,2888/TCP,3888/TCP   15m
	service/mysql1                          LoadBalancer   10.107.225.68    localhost     3306:30743/TCP               15m

	NAME                         READY   UP-TO-DATE   AVAILABLE   AGE
	deployment.apps/myservice1   1/1     1            1           15m
	deployment.apps/mysql1       1/1     1            1           15m

	NAME                                    DESIRED   CURRENT   READY   AGE
	replicaset.apps/myservice1-67cc9f58db   1         1         1       15m
	replicaset.apps/mysql1-675ff8696c       1         1         1       15m

	NAME                                    READY   AGE
	statefulset.apps/myservice1-kafka       1/1     15m
	statefulset.apps/myservice1-zookeeper   1/1     15m

$ kubectl -n hello port-forward service/kafka1 9092:9092

$ kubectl -n hello port-forward service/myservice1 9082:8082

$ curl -X GET "http://localhost:9082/kafka/post?title=title3&desc=description%20Of%20T3"
	; then the table posts will creat a new records:

$ kubectl -n hello exec -it service/mysql1 -- bash
	mysql --user=user1 --password='V4321abcd!' -e "show databases;"

	mysql --user=user1 --password='V4321abcd!'
	use MyDB;
	SELECT VERSION(), CURRENT_DATE, user();
	show tables;
	SELECT * FROM comments;

	mysql> SELECT * FROM posts;

$ helm uninstall myservice1 --namespace hello

-------------------------------------------------------------------------
