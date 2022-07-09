#FROM maven:3.6.1-jdk-11 AS build
#RUN mkdir -p workspace
#WORKDIR workspace
#COPY ./pom.xml /workspace
#COPY ./src /workspace/src
#RUN mvn -f pom.xml clean install -DskipTests
#
#FROM openjdk:11
#COPY --from=build /workspace/target/*.jar innovator-backend.jar
#EXPOSE 8888
#ENTRYPOINT ["java","-jar","innovator-backend.jar"]

#FROM openjdk:11
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} Innovator-0.0.1.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/Innovator-0.0.1.jar"]

FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Innovator-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/Innovator-0.0.1.jar"]