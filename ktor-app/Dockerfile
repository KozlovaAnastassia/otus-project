FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/ktor-app.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]