FROM arm64v8/openjdk:17-slim

WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Copy the pom.xml and source code
COPY ./pom.xml ./
COPY ./src ./src

# Package the application
RUN mvn clean package -DskipTests

# Runtime configuration
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/target/receipt-processor-1.0.0.jar"]