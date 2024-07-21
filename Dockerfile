FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
VOLUME /logs
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]