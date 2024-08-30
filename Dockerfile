FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
VOLUME /home/ubuntu/logs
VOLUME /usr/bin/ffprobe
VOLUME /usr/bin/ffmpeg
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]