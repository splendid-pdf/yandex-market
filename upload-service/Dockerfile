FROM openjdk:17-alpine
WORKDIR /app
EXPOSE 9001
COPY build/libs/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]