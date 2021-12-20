#FROM openjdk:11
#ENV TZ=Europe/Kiev
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} Innovator.jar
#ENTRYPOINT ["java", "-jar", "/Innovator.jar"]

FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Innovator-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/Innovator-0.0.1.jar"]