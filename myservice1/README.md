# Getting Started

### About tables in MySql

winpty docker exec -it mysql1 mysql --user=user1 --password='V4321abcd!'

show databases;
use MyDB;
show tables;

mysql> select * from posts;
+----+----------------------+--------+
| id | description          | title  |
+----+----------------------+--------+
|  1 | description of post1 | title1 |
+----+----------------------+--------+
1 row in set (0.00 sec)

mysql> select * from comments;
+----+-----------+--------+
| id | text      | pc_fid |
+----+-----------+--------+
| 1  | comment 1 |      1 |
| 2  | comment 2 |      1 |
+----+-----------+--------+

### About Kafka
    https://github.com/eugenp/tutorials/tree/master/spring-kafka
	https://www.baeldung.com/spring-kafka

## How to Run on Docker
 - For myservice1:
	cd ./myservice1	
	mvn clean package -DskipTests
	
	docker-compose -f docker-compose.yml build
	docker-compose -f docker-compose.yml up -d
	; then, go to http://localhost:8082/ which has links to send messages
	
	; if the website is not started, check MySql DB:
	  (e.g. from docker logs myservice1 :
     org.hibernate.exception.GenericJDBCException: Unable to open JDBC Connection for DDL execution)
	  
		winpty docker exec -it mysql1 mysql --user=user1 --password='V4321abcd!'
		show databases;
	; then restart myservice1 in Docker.
	
	; this url send message to post-topic, then the post is saved to MySql [post] table
	http://localhost:8082/kafka/post?title=title2&desc=descriptionOfT2

   == Test with 	
	1. load the MySql and Kafka compose to container and run (from either Command window or PowerShell )
		; docker-compose-v0.yml is ok when Kafka is in Docker container and debug/run myservice1 from IntelliJ
		
		git clone https://github.com/MorganZhou73/MySpringApp2.git
		cd MySpringApp2\myservice1
		docker-compose -f docker-compose-v1.yml up -d
		
	2. check MySql DB (optional)
		winpty docker exec -it mysql1 mysql --user=user1 --password='V4321abcd!'
		show databases;
	
	3. compile/run myservice1 to generate target/*.jar from GitBash
		cd ./myservice1
		
		mvn clean package
	
		mvn clean package -DskipTests
		
		mvn test
		
		; Run the unit tests to initiate MySql and topics of Kafka.
		mvn -Dtest=Myservice1ApplicationTests test

		; run a single unit test
		mvn -Dtest=PostKafkaTest#sendPostTest test
		
		java -jar target/myservice1-0.0.1-SNAPSHOT.jar --server.port=8082
		; then, go to http://localhost:8082/ which has links to send messages
		
		; check kafka topic and listen(consume) topic "message-topic"
		winpty docker exec -it kafka1 bash
		kafka-topics.sh --bootstrap-server localhost:9092 --list

		kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic message-topic
		
		; from another window to listen a new topic
		kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic partition-topic
		
		kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic filter-topic
		
		kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic post-topic

		http://localhost:8082/kafka/message?msg=good%20morning
		http://localhost:8082/kafka/filter?msg=filterHellojoe!
		http://localhost:8082/kafka/filter?msg=filterHelloWorld!
		http://localhost:8082/kafka/post?title=title2&desc=descriptionOfT2
		http://localhost:8082/kafka/greeting?name=greeting&msg=howAreYou
		
	3. run myservice3 in Docker	
		docker-compose -f docker-compose-v1.yml up -d
		
		; make image for myservice3:tag-1.0.0
		docker build -t myservice1:tag-1.0.0 .
		
		docker run --network net-mysql  -e "KAFKA_URI=kafka1:29092" -e "mysql-servername=mysql1" -e "mysql-db=MyDB" -e "spring_datasource_username=user1" -e "spring_datasource_password=V4321abcd!" -e "spring_datasource_url=jdbc:mysql://mysql1:3306/MyDB?useSSL=false&allowPublicKeyRetrieval=true" -p 8082:8082 --name myservice1 -d myservice1:tag-1.0.0
		
		https://www.javainuse.com/devOps/docker/docker-mysql
		https://medium.com/@marcelo.hossomi/running-kafka-in-docker-machine-64d1501d6f0b
		https://www.confluent.io/blog/kafka-client-cannot-connect-to-broker-on-aws-on-docker-etc/
		
		docker network ls
		
		; check network name of a container
		docker inspect mysql1 -f "{{json .NetworkSettings.Networks }}"
		
		docker logs myservice1
		docker stop myservice1
		docker rm myservice1

### Guides
 

