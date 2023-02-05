FROM openjdk:17-alpine
WORKDIR /app
EXPOSE 9090
COPY build/libs/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]