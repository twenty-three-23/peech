FROM openjdk:17-bullseye
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
VOLUME /home/ubuntu/logs
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]