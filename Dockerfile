# Build stage
FROM gradle:8.7-alpine as builder
WORKDIR /build

COPY build.gradle.kts settings.gradle.kts /build/
COPY . /build

RUN gradle build -x test

# Final stage
FROM openjdk:22-jdk-slim
WORKDIR /app

COPY --from=builder /build/build/libs/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]