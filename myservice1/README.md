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

### Push to Docker hub https://hub.docker.com/
		docker build -t zmg9046/myservice1:tag-1.0.0 .
		
		docker run --network net-mysql  -e "KAFKA_URI=kafka1:29092" -e "mysql-servername=mysql1" -e "mysql-db=MyDB" -e "spring_datasource_username=user1" -e "spring_datasource_password=V4321abcd!" -e "spring_datasource_url=jdbc:mysql://mysql1:3306/MyDB?useSSL=false&allowPublicKeyRetrieval=true" -p 8082:8082 --name myservice1 -d zmg9046/myservice1:tag-1.0.0
		
		docker login
			; input the user account /password to https://hub.docker.com/ 
			
		docker push zmg9046/myservice1:tag-1.0.0
		
## Helm chart : MySpringApp2\myservice1
	https://github.com/bitnami/charts/
	$ helm repo add bitnami https://charts.bitnami.com/bitnami
	$ helm repo list
	$ helm repo update	
	
### create the helm charts
	$ helm search repo mysql
	$ helm search repo kafka
	NAME                            CHART VERSION   APP VERSION     DESCRIPTION
	bitnami/kafka                   12.12.0         2.7.0           Apache Kafka is a distributed streaming platform.
	
$ kubectl create ns hello
$ helm ls --namespace hello

$ kompose -f "docker-compose.yml" convert
	INFO Kubernetes file "kafka1-service.yaml" created
	INFO Kubernetes file "myservice1-service.yaml" created
	INFO Kubernetes file "mysql1-service.yaml" created
	INFO Kubernetes file "zookeeper-service.yaml" created
	INFO Kubernetes file "kafka1-deployment.yaml" created
	INFO Kubernetes file "myservice1-deployment.yaml" created
	INFO Kubernetes file "mysql1-deployment.yaml" created
	INFO Kubernetes file "zookeeper-deployment.yaml" created

$ helm create service1chart
$ helm lint				: to check whether the current helm chart is legal

$ helm package service1chart
$ helm package mysql
$ helm package kafka

$ mv kafka-12.11.0.tgz myservice1/charts/kafka-12.11.0.tgz
$ mv mysql-0.1.0.tgz myservice1/charts/mysql-0.1.0.tgz
$ mv service1chart-0.1.0.tgz myservice1/charts/service1chart-0.1.0.tgz

### install the helm charts to Kubernetes
$ helm install myservice1 myservice1 --namespace hello

	$ helm --namespace hello ls
	NAME            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART                         APP VERSION
	myservice1      hello           1               2021-04-03 15:42:41.8612772 -0400 EDT   deployed        myservice1app-0.1.0-SNAPSHOT   
	
	$ kubectl -n hello get pvc
	No resources found in hello namespace.
	
	$ kubectl -n hello get all
	NAME                              READY   STATUS    RESTARTS   AGE
	pod/myservice1-67cc9f58db-9gvrc   1/1     Running   5          9m50s
	pod/myservice1-kafka-0            1/1     Running   1          9m50s
	pod/myservice1-zookeeper-0        1/1     Running   0          9m50s
	pod/mysql1-675ff8696c-8lz6x       1/1     Running   0          9m50s
	
	NAME                                    TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
	service/kafka1                          ClusterIP      10.99.76.141    <none>        9092/TCP                     9m51s
	service/myservice1                      ClusterIP      10.110.192.8    <none>        8082/TCP                     9m51s
	service/myservice1-kafka-headless       ClusterIP      None            <none>        9092/TCP,9093/TCP            9m51s
	service/myservice1-zookeeper            ClusterIP      10.102.99.136   <none>        2181/TCP,2888/TCP,3888/TCP   9m50s
	service/myservice1-zookeeper-headless   ClusterIP      None            <none>        2181/TCP,2888/TCP,3888/TCP   9m51s
	service/mysql1                          LoadBalancer   10.98.146.233   localhost     3306:30205/TCP               9m50s
	
	NAME                         READY   UP-TO-DATE   AVAILABLE   AGE
	deployment.apps/myservice1   1/1     1            1           9m50s
	deployment.apps/mysql1       1/1     1            1           9m50s
	
	NAME                                    DESIRED   CURRENT   READY   AGE
	replicaset.apps/myservice1-67cc9f58db   1         1         1       9m50s
	replicaset.apps/mysql1-675ff8696c       1         1         1       9m50s
	
	NAME                                    READY   AGE
	statefulset.apps/myservice1-kafka       1/1     9m50s
	statefulset.apps/myservice1-zookeeper   1/1     9m50s

$ kubectl -n hello port-forward service/kafka1 9092:9092
$ kubectl -n hello port-forward service/myservice1-zookeeper 2181:2181

$ kubectl -n hello port-forward service/myservice1 9082:8082

$ curl -X GET "http://localhost:9082/kafka/post?title=title3&desc=description%20Of%20T3"
; then the DB table [posts] will creat a new records:

$ kubectl -n hello exec -it service/mysql1 -- bash
	mysql --user=user1 --password='V4321abcd!' -e "show databases;"
	use MyDB;
	SELECT VERSION(), CURRENT_DATE, user();
	show tables;
	
	mysql> SELECT * FROM posts;
	+----+-------------------+--------+
	| id | description       | title  |
	+----+-------------------+--------+
	|  1 | description Of T2 | title2 |
	|  2 | description Of T3 | title3 |

$ helm uninstall myservice1 --namespace hello
