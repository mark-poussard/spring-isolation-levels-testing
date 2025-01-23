# Use a base image with Java runtime
FROM openjdk:24-jdk-slim

# Set the working directory
WORKDIR /app

# Copy build output to the container
COPY build/libs/app.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]