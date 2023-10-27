# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled Spring Boot application JAR file into the container at /app
COPY target/bukins-api-0.0.1.jar bukins-api.jar

# Expose the port that your Spring Boot application will run on (default is 8080)
EXPOSE 8083

# Specify the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "bukins-api.jar"]
