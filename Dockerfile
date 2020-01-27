FROM openjdk:10-jre-slim
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Xmx512m", "-jar", "/app.jar"]
