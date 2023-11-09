FROM openjdk:11-jdk-slim AS build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw
RUN ./mvnw clean package

FROM openjdk:11-jre-slim
COPY --from=build /workspace/app/target/sr-tool-0.0.1.jar /sr-tool-0.0.1.jar
EXPOSE 8080

CMD ["/bin/sh", "-c", "java -jar -Dspring.profiles.active=docker sr-tool-0.0.1.jar"]
