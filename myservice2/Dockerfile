# For Java 8, try this
FROM openjdk:8-jdk-alpine

# For Java 11, try this
# FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER unistar.com

VOLUME /tmp
EXPOSE 8081

COPY target/*.jar myservice2.jar

ENTRYPOINT java -jar myservice2.jar
