FROM openjdk:11-jdk-slim AS build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw
RUN ./mvnw clean package

FROM openjdk:11-jre-slim
COPY --from=build /workspace/app/target/app.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]