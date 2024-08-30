FROM openjdk:17-bullseye

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    ffmpeg \
    && apt-get clean && rm -rf /var/lib/apt/lists/* \

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
VOLUME /home/ubuntu/logs
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]