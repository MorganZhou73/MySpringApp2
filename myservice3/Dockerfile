# For Java 8, try this
FROM openjdk:8-jdk-alpine

# For Java 11, try this
# FROM adoptopenjdk/openjdk11:alpine-jre

RUN apk add --no-cache bash

MAINTAINER unistar.com

VOLUME /tmp
EXPOSE 8080

COPY ./wait-for-it.sh /wait-for-it.sh
RUN chmod +x wait-for-it.sh

COPY target/*.jar myservice3.jar

ENTRYPOINT java -jar myservice3.jar
