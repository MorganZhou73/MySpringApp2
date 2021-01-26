# My Spring RESTfull Web Service accessing Mongo DB, WebSecurityConfig
		to demo Spring MVC, WebSecurityConfigurerAdapter, healthcheck
		receive message from RabbitMQ, and save to MongoDB

http://localhost:8080/
    GreetingController: RESTfull Web Service, CrossOrigin, (/greeting ; /greeting1/{name} )
        sendmessage in String/Json/Xml/Map demo (/sendmessage ;  /sendmessagemap )
            https://spring.io/guides/gs/rest-service/
            https://spring.io/guides/gs/rest-service-cors/
            https://spring.io/blog/2015/06/08/cors-support-in-spring-framework
            http://www.javaear.com/question/32319396.html
            https://stackoverflow.com/questions/39457121/spring-security-multiple-url-ruleset-not-working-together
            
    GreetingTest : sample to create API documentation with Restdocs
            https://spring.io/guides/gs/testing-restdocs/
    
    Greeting2Controller (/hello; /greeting2 ; /searchPeople): sample of Spring MVC only, no REST
      - Controller integrated with Thymeleaf pages
            https://spring.io/guides/gs/serving-web-content/
    Greeting2Test: sample of @MockBean and @WebMvcTest
            https://spring.io/guides/gs/testing-web/
            
    PersonRepository (/people ) : web service connect Mongo DB collection (person)
            https://spring.io/guides/gs/accessing-mongodb-data-rest/
            https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongodb.repositories.queries
                        
    EmployeesRepository (/employee) : web service connect Mongo DB collection (employees)
        MongoRepository will auto create endpoints /employee/search;  /profile/employee
        
    EmployeesController (/employees) : customized contoller: Spring MVC + Spring HATEOAS
            https://spring.io/guides/tutorials/bookmarks/
            https://medium.com/@depu19/customizing-spring-data-mongodb-e75c729dc861
            
    management services by actuator module:
            https://spring.io/guides/gs/spring-boot/
            
    WebSecurityConfig:
        https://spring.io/guides/gs/securing-web/
        after adding WebSecurityConfigurerAdapter, to pass the unit test: 
            . the unit tests have to add @WithMockUser Or add permission in configure()
            . for the POST method, need http.csrf().disable() in configure()

    RabbitMQ:
        https://thepracticaldeveloper.com/produce-and-consume-json-messages-with-spring-boot-amqp/#receiving-messages
        https://github.com/mechero/spring-boot-amqp-messaging
##--------------------------------------------------
## Compile/Run Commands in Bash command line
./mvnw clean package
java -jar target/myservice2-0.0.1-SNAPSHOT.jar --server.port=8080

### compile without running test cases
./mvnw clean package -DskipTests
./mvnw clean package -Dmaven.test.skip=true

### CrossOrigin Test
CD myservice2
XCOPY ".\target\myservice2-0.0.1-SNAPSHOT.jar" "..\backup\" /S /Y

; start 2 services with the same jar
java -jar "..\backup\myservice2-0.0.1-SNAPSHOT.jar" --server.port=9000
java -jar target/myservice2-0.0.1-SNAPSHOT.jar --server.port=8080

; directly accessing the 2 services, verify both get correct Greeting object:
http://localhost:9000/greeting
http://localhost:8080/greeting

; origin :9000 to access :8000/greeting will get nothing:
http://localhost:9000/greeting2?name=joe&port=8080

; origin :8080 to access :9000/greeting will get correct Greeting object:
http://localhost:8080/greeting2?name=joe&port=8080

D:\Temp>curl "http://localhost:9000/greeting" -H "Origin: http://localhost:8080"
{
  "id" : 4,
  "content" : "Good morning, World!"
}

D:\Temp>curl "http://localhost:9000/greeting" -H "Origin: http://localhost:8000"
Invalid CORS request

; except /greeting which declared as @CrossOrigin(origins = "http://localhost:8080"),
; other endpoint permit any Origin
D:\Temp>curl "http://localhost:9000/hello" -H "Origin: http://localhost:8000"
Hello, World
D:\Temp>curl "http://localhost:9000/greeting-javaconfig" -H "Origin: http://localhost:8000"
{
  "id" : 3,
  "content" : "Good morning, World!"
}

##--------------------------------------------------
## About Test annotation    
     . using the @AutoConfigureMockMvc and MockMvc, code will be called in exactly the same way as if it were processing a real HTTP request but without the cost of starting the server
     . using @WebMvcTest, the full Spring application context is started but without the server. We can narrow the tests to only the web layer rather than the whole context
     
##--------------------------------------------------
## curl Test in command window
curl http://localhost:8080/greeting?name=morgan

curl -c ./cookie01 -d "username=user1" -d "password=secret123" "http://localhost:8080/login"
curl http://localhost:8080/greeting1/joe -b ./cookie01

curl http://localhost:8080/greeting2 -b ./cookie01

curl http://localhost:8080

curl localhost:8080/actuator/health
curl localhost:8080/actuator/info
curl -X POST localhost:8080/actuator/shutdown

curl -X POST -H "Content-Type:application/json" -d "{ \"id\" : \"content\" }" http://localhost:8080/people

curl localhost:8080/healthcheck?format=short -v
curl localhost:8080/healthcheck?format=full -v
curl localhost:8080/healthcheck -v

D:\Temp>curl localhost:8080/healthcheck
{
  "timestamp" : "2021-01-13T15:39:17.662+00:00",
  "status" : 400,
  "error" : "Bad Request",
  "message" : "",
  "path" : "/healthcheck"
}

D:\Temp>curl localhost:8080/healthcheck?format=full -v
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /healthcheck?format=full HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
>
< HTTP/1.1 200
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Wed, 13 Jan 2021 22:39:10 GMT
<
{
  "currentTime" : "2021-01-13T22:39:10Z",
  "application" : "OK"
}* Connection #0 to host localhost left intact

D:\Temp>curl localhost:8080/healthcheck1?format=full -v
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /healthcheck1?format=full HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
>
< HTTP/1.1 200
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Wed, 13 Jan 2021 22:39:39 GMT
<
{
  "currentTime" : "2021-01-13T22:39:39Z",
  "application" : "OK"
}* Connection #0 to host localhost left intact

D:\Temp>curl localhost:8080/healthcheck2?format=full -v
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /healthcheck2?format=full HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
>
< HTTP/1.1 200
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: application/json
< Content-Length: 63
< Date: Wed, 13 Jan 2021 22:49:41 GMT
<
{"currentTime":"2021-01-13T22:49:41.768Z", "application": "OK"}* Connection #0 to host localhost left intact

##--------------------------------------------------
## query for all people
curl http://localhost:8080/people

#### after WebSecurityConfig, need to access the login page and save the cookie to access other pages from cURL:
curl -c ./cookie01 -d "username=user1" -d "password=secret123" "http://localhost:8080/login"
curl -b ./cookie01 "http://localhost:8080/employees/1"

; the following curl commands with -u NOT working !!!:
    curl http://localhost:8080/people -u user1:secret123 -v -L --max-redirs 2
    curl --netrc-file .netrc http://localhost:8080/people
    curl http://user1:secret123@localhost:8080/people -v
    curl -n http://localhost:8080/people
    
    curl http://localhost:8080/people -u user1:secret123 -v -L --cookie-jar ./cookie01
    curl http://localhost:8080/people -v -L --cookie ./cookie01

## POST REST calls to sendmessage
curl -X POST -H "Content-Type:application/json" -d "{ \"id\" : 1, \"content\" :  \"Good morning\" }" "http://localhost:8080/sendmessage" -b ./cookie01
curl -X POST -H "Content-Type:application/json" -d "@.\src\test\resources\message1.json" "http://localhost:8080/sendmessage" -b ./cookie01

curl -X POST -H "Content-Type:text/plain" -d "Good morning" "http://localhost:8080/sendmessage" -b ./cookie01
curl -X POST -H "Content-Type:application/xml" -d "<message><id>1</id><content>Good morning</content></message>" "http://localhost:8080/sendmessage" -b ./cookie01

curl -X POST -d "id=1&content=good morning" "http://localhost:8080/sendmessagemap" -b ./cookie01

### test content for jQuery in home2.html and AngularJS in home.html :
Url:	/sendmessage
Post data: (button "Send an HTTP POST 2")
	Good morning
	{ "id": "2", "content": "good morning, buddy"}
	<message><id>3</id><content>Good evening</content></message>

Url:	/people
Post data: 
	{ "firstName": "John", "lastName": "White"}

Url:	/employees
Post data: (button "Send an HTTP POST 2")
	{ "employeeID": 101, "firstName": "Jack", "lastName":"Baggins", "title": "Sales Rep", "birthDate": "1972-01-23", "city": "Markham", "reportsTo": 2 }

then can try
	http://localhost:8080/employees/101
	
### POST REST calls to create a new record in collection [person]; 
### if the collection not exist, create the collection [person] first. 
curl -i -X POST -H "Content-Type:application/json" -d "{  \"firstName\" : \"Frodo\",  \"lastName\" : \"Baggins\" }" http://localhost:8080/people -b ./cookie01

curl -X POST -H "Content-Type:application/json" -d "{ \"lastName\" : \"Baggins\" }" http://localhost:8080/people -b ./cookie01

### GET call for the individual record 
curl http://localhost:8080/people/search/findByLastName?name=Baggins -b ./cookie01
curl http://localhost:8080/people/5f713b863ea6b05b0dcd160b -b ./cookie01
curl -X GET http://localhost:8080/people/search/findByLastName?name=Baggins -b ./cookie01

### issue PUT REST calls to replace the entire record; Fields not supplied will be replaced with null
curl -X PUT -H "Content-Type:application/json" -d "{ \"firstName\": \"Bilbo\", \"lastName\": \"Baggins\" }" http://localhost:8080/people/5f713b863ea6b05b0dcd160b -b ./cookie01

curl -X PUT -H "Content-Type:application/json" -d "{\"lastName\": \"Baggins\" }" http://localhost:8080/people/5f713b863ea6b05b0dcd160b -b ./cookie01

### issue PATCH REST calls to update a subset of items
curl -X PATCH -H "Content-Type:application/json" -d "{ \"firstName\": \"Bilbo Jr.\" }" http://localhost:8080/people/5f713b863ea6b05b0dcd160b -b ./cookie01

### Notes: PATCH call with search will NOT update any records !!!
curl -X PATCH -H "Content-Type:application/json" -d "{ \"firstName\": \"Jane\" }" http://localhost:8080/people/search/findByLastName?name=Baggins -b ./cookie01

### issue DELETE REST calls to delete existing records
curl -X DELETE http://localhost:8080/people/5f713b863ea6b05b0dcd160b -b ./cookie01

##--------------------------------------------------
## import Northwind.employees from employees.csv
## MongoDB will never import null values from CSV data.

mongoimport -d Northwind -c "employees" --type csv --file "employees.csv" --headerline

## Or change the .csv to add type info to the headerline and with  --columnsHaveTypes
## this command will ignore the document/record which has NULL for int type field,
## so need change the ReportsTo of 2nd document from NULL to 0
mongoimport -d Northwind -c "employees" --type csv --file "employees1.csv" --headerline --columnsHaveTypes

## the BirthDate/HireDate is set as String in employees1.csv and can't use specify date() when mongoimport
mongo.exe convertDate.js

mongo.exe --eval "db=db.getSiblingDB('Northwind');db.employees.find({$and: [{'LastName':'Davolio'},{'FirstName':'Nancy'}]});"
mongo.exe --eval "db=db.getSiblingDB('Northwind');db.employees.find({'LastName':'Dodsworth'});"

curl http://localhost:8080/employee -b ./cookie01

// endpoint /employee is autogenerated, the employeeID of 2 documents could be same;
// then /employees/100 may get 2 docments, cause Internal Server Error : status=500
curl -X POST -H "Content-Type:application/json" -d "{ \"employeeID\": 100, \"firstName\": \"Frodo\", \"lastName\":\"Baggins\", \"title\": \"Sales Rep\", \"birthDate\": \"1970-01-23\", \"city\": \"Toronto\", \"reportsTo\": 1 }" http://localhost:8080/employee -b ./cookie01

curl -X GET "http://localhost:8080/employee/search/findByLastName?name=Baggins" -b ./cookie01

curl -X GET "http://localhost:8080/employee/search/findByEmployeeID?employeeID=100" -b ./cookie01

// following 4 URLs all get the 1st person
curl -X GET "http://localhost:8080/employee/search/findByEmployeeID?employeeID=1" -b ./cookie01
curl -X GET "http://localhost:8080/employee/search/findByQueryFnLn?fn=Nancy&ln=Davolio" -b ./cookie01
curl -X GET "http://localhost:8080/employee/search/findByLnOrFn?name=Nancy"
curl -X GET "http://localhost:8080/employee/search/findByLnOrFn?name=Davolio"

curl "http://localhost:8080/employee?page=1&size=2"

curl -v "http://localhost:8080/employees/1"
curl -v "http://localhost:8080/employees/10"
curl -v "http://localhost:8080/employees/findByProperties?city=London&page=0&size=1"

curl -v "http://localhost:8080/employee/search/findByProperties?city=London&page=0&size=2&sort=lastName,desc"
curl -v "http://localhost:8080/employee/search/findByProperties?city=London&title=Sales%20Representative"

curl -X POST -H "Content-Type:application/json" -d "{ \"employeeID\": 101, \"firstName\": \"Jack\", \"lastName\":\"Baggins\", \"title\": \"Sales Rep\", \"birthDate\": \"1972-01-23\", \"city\": \"Markham\", \"reportsTo\": 2 }" http://localhost:8080/employees
curl -X PUT -H "Content-Type:application/json" -d "{ \"firstName\": \"Jackson\", \"lastName\":\"Baggins\", \"title\": \"Sales VP\", \"reportsTo\": 1 }" http://localhost:8080/employees/101
curl -v -X DELETE localhost:8080/employees/101

curl -v -X POST -H "Content-Type:application/json" -d "{ \"employeeID\": 102, \"name\": \"Marina Lin\", \"title\": \"Manager\", \"birthDate\": \"1976-01-23\", \"city\": \"Markham\", \"reportsTo\": 2 }" http://localhost:8080/employees
curl -v -X PUT -H "Content-Type:application/json" -d "{ \"name\": \"Mary Leen\", \"title\": \"RD VP\", \"reportsTo\": 1 }" http://localhost:8080/employees/102
 
##--------------------------------------------------
## when no index.html, autogenerated RepositoryIndex homepage would be like:
{
  "_links" : {
    "people" : {
      "href" : "http://localhost:8080/people{?page,size,sort}",
      "templated" : true
    },
    "employee" : {
      "href" : "http://localhost:8080/employee{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile"
    }
  }
}

## MongoRepository generated contents for http://localhost:8080/people
{
  "_embedded" : {
    "people" : [ {
      "firstName" : "Bilbo Jr.",
      "lastName" : "Baggins",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/people/5f7644c6718cdf79c453cb97"
        },
        "person" : {
          "href" : "http://localhost:8080/people/5f7644c6718cdf79c453cb97"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/people"
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/people"
    },
    "search" : {
      "href" : "http://localhost:8080/people/search"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 1,
    "totalPages" : 1,
    "number" : 0
  }  
}
##--------------------------------------------------
C:\>curl http://localhost:8080/employee/search
{
  "_links" : {
    "findByQueryFnLn" : {
      "href" : "http://localhost:8080/employee/search/findByQueryFnLn{?fn,ln}",
      "templated" : true
    },
    "findByLastName" : {
      "href" : "http://localhost:8080/employee/search/findByLastName{?name}",
      "templated" : true
    },
    "findByEmployeeID" : {
      "href" : "http://localhost:8080/employee/search/findByEmployeeID{?employeeID}",
      "templated" : true
    },
    "findByLnOrFn" : {
      "href" : "http://localhost:8080/employee/search/findByLnOrFn{?name,name}",
      "templated" : true
    },
    "findByProperties" : {
      "href" : "http://localhost:8080/employee/search/findByProperties{?firstName,lastName,title,city,page,size,sort}",
      "templated" : true
    },
    "self" : {
      "href" : "http://localhost:8080/employee/search"
    }
  }
}

##--------------------------------------------------
### About Unit Test
How to test services, endpoints, and repositories in Spring Boot:
    https://www.freecodecamp.org/news/unit-testing-services-endpoints-and-repositories-in-spring-boot-4b7d9dc2b772/
    
    https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/#introduction
    

