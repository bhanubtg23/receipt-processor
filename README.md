**Receipt Processor Application Setup Guide**
**Getting Started**

Clone the repository:

```bash
git clone https://github.com/bhanubtg23/receipt-processor.git
cd receipt-processor
```

**System Requirements & Installation**
**For Mac with Apple Silicon (M1/M2)**

1. Install Docker Desktop for Mac (Apple Silicon):

Download from Docker Desktop
Install and start Docker Desktop
Wait for Docker Desktop to fully initialize (green light in status bar)


2. Use this Dockerfile (compatible with ARM64) - already included in the repo:
```bash
FROM arm64v8/openjdk:17-slim

WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

COPY ./pom.xml ./
COPY ./src ./src

RUN mvn clean package -DskipTests

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/target/receipt-processor-1.0.0.jar"]
```

**For Windows/Intel Mac**

1. Install Docker Desktop:

Download from Docker Desktop
Install and start Docker Desktop


2. Modify the Dockerfile in the repo to:
```bash
FROM openjdk:17-slim

WORKDIR /app

RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

COPY ./pom.xml ./
COPY ./src ./src

RUN mvn clean package -DskipTests

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/target/receipt-processor-1.0.0.jar"]
```

**For Linux**

Install Docker Engine:
```bash
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io
sudo systemctl start docker
```

2. Use the same Dockerfile as Windows/Intel Mac (above)

**Running the Application**

1. After cloning the repository and ensuring you have the correct Dockerfile for your system:
2. Build the Docker image:
```bash
docker build -t receipt-processor .
```
3. Run the container:
```bash
docker run -p 8080:8080 receipt-processor
```

**Testing the Application**
Test the API endpoints using curl:

1. Process a receipt:
```bash
curl -X POST http://localhost:8080/receipts/process \
-H "Content-Type: application/json" \
-d '{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },
    {
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    }
  ],
  "total": "18.74"
}'
```
2. Get points (replace {id} with the ID from previous response):
```bash
curl http://localhost:8080/receipts/{id}/points
```
