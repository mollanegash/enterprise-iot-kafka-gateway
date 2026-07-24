# Enterprise IoT Kafka Gateway

A production-style **IoT telemetry processing platform** built with **Spring Boot, Apache Kafka, H2 Database, Docker, and Terraform**.

The system simulates IoT sensor devices publishing telemetry data, processes messages through Kafka, detects critical temperature events, and stores telemetry history in a database.

---

## Architecture Overview

```
                   +----------------+
                   | IoT Devices    |
                   | Sensor Data     |
                   +-------+--------+
                           |
                           |
                           v
                 +-------------------+
                 | Spring Boot       |
                 | Kafka Producer    |
                 +-------------------+
                           |
                           |
                           v
                 +-------------------+
                 | Apache Kafka      |
                 | Topic:            |
                 | iot-telemetry-   |
                 | stream            |
                 +-------------------+
                           |
                           |
                           v
                 +-------------------+
                 | Kafka Consumer    |
                 | Spring Service    |
                 +-------------------+
                           |
                           |
                           v
                 +-------------------+
                 | H2 Database       |
                 | Telemetry Storage |
                 +-------------------+


Deployment:

              Terraform
                  |
                  |
     +------------+-------------+
     |                          |
     v                          v
 Docker Network            Docker Containers

                           +-------------+
                           | ZooKeeper   |
                           +-------------+

                           +-------------+
                           | Kafka       |
                           +-------------+

                           +-------------+
                           | Spring Boot |
                           | IoT Gateway |
                           +-------------+
```

---

# Features

## IoT Telemetry Streaming

The application supports real-time sensor telemetry:

Example message:

```json
{
  "deviceId": "IOT-DEVICE-1",
  "location": "Zone-A",
  "temperature": 94.5,
  "pressure": 1011.5,
  "status": "CRITICAL",
  "timestamp": "2026-07-24T21:54:38Z"
}
```

---

## Kafka Integration

Kafka is used as the messaging backbone.

Topic:

```
iot-telemetry-stream
```

Producer:

```
KafkaProducerService
```

Consumer:

```
KafkaConsumerService
```

Flow:

```
Sensor Data
     |
     v
Kafka Producer
     |
     v
Kafka Topic
     |
     v
Kafka Consumer
     |
     v
Database Storage
```

---

# Critical Alert Detection

The consumer checks telemetry status.

Example:

```java
if ("CRITICAL".equals(telemetry.getStatus())) {

    log.warn(
      "CRITICAL ALERT! Device {} exceeded safe temperature limits",
      telemetry.getDeviceId()
    );

}
```

Example output:

```
🚨 CRITICAL ALERT!
Device IOT-DEVICE-1 exceeded safe temperature limits
Current Temp: 99.9°C
```

---

# Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Application runtime |
| Spring Boot | Backend framework |
| Spring Kafka | Kafka integration |
| Apache Kafka | Event streaming |
| ZooKeeper | Kafka coordination |
| Spring Data JPA | Database access |
| H2 Database | Telemetry storage |
| Docker | Containerization |
| Terraform | Infrastructure automation |

---

# Project Structure

```
enterprise-iot-kafka-gateway
│
├── src
│   └── main
│       └── java
│           └── com.example.enterprise_iot_kafka_gateway
│
│               ├── controller
│               │     └── SensorController.java
│               │
│               ├── consumer
│               │     └── KafkaConsumerService.java
│               │
│               ├── producer
│               │     └── KafkaProducerService.java
│               │
│               ├── model
│               │     ├── SensorTelemetry.java
│               │     └── SensorTelemetryEntity.java
│               │
│               └── repository
│                     └── SensorRepository.java
│
├── terraform
│   └── main.tf
│
├── Dockerfile
│
├── pom.xml
│
└── README.md
```

---

# Running Locally

## Requirements

Install:

- Java 21
- Maven
- Docker
- Terraform

Verify:

```bash
java -version

docker --version

terraform --version
```

---

# Build Application

From project root:

```bash
mvn clean package
```

This creates:

```
target/*.jar
```

---

# Docker Image Build

Build the application image:

```bash
docker build \
-t enterprise-iot-kafka-gateway .
```

Verify:

```bash
docker images | grep enterprise-iot-kafka-gateway
```

---

# Deploy Infrastructure Using Terraform

Navigate:

```bash
cd terraform
```

Initialize Terraform:

```bash
terraform init
```

Preview deployment:

```bash
terraform plan
```

Deploy:

```bash
terraform apply
```

Confirm:

```bash
docker ps -a
```

Expected containers:

```
iot-gateway-app
kafka
zookeeper
```

---

# Docker Deployment Architecture

Terraform creates:

## Kafka Network

```
iot-kafka-net
```

## Containers

### ZooKeeper

```
Image:
confluentinc/cp-zookeeper:7.5.0
```

Port:

```
2181
```

---

### Kafka Broker

```
Image:
confluentinc/cp-kafka:7.5.0
```

Port:

```
9092
```

Kafka address inside Docker:

```
kafka:9092
```

---

### Spring Boot Application

Container:

```
iot-gateway-app
```

Port:

```
8080
```

Kafka connection:

```
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

---

# REST API

## Get Stored Telemetry

Endpoint:

```
GET /api/telemetry
```

Example:

```bash
curl http://localhost:8080/api/telemetry
```

Response:

```json
[
 {
   "deviceId":"IOT-DEVICE-1",
   "location":"Zone-A",
   "temperature":92.7,
   "pressure":1004,
   "status":"CRITICAL"
 }
]
```

---

# H2 Database Console

Enable:

```
spring.h2.console.enabled=true
```

Access:

```
http://localhost:8080/h2-console
```

Database:

```
jdbc:h2:mem:testdb
```

Username:

```
sa
```

Password:

```
(empty)
```

---

# Kafka Configuration

Application properties:

```properties
spring.kafka.bootstrap-servers=kafka:9092

spring.kafka.consumer.group-id=iot-gateway-group

spring.kafka.consumer.auto-offset-reset=earliest

spring.kafka.consumer.value-deserializer=
org.springframework.kafka.support.serializer.JsonDeserializer


spring.kafka.producer.value-serializer=
org.springframework.kafka.support.serializer.JsonSerializer
```

---

# Testing Kafka Telemetry

Example telemetry:

```json
{
 "deviceId":"sensor-001",
 "location":"factory-floor-1",
 "temperature":95,
 "pressure":30,
 "status":"CRITICAL",
 "timestamp":"2026-07-24T21:55:00"
}
```

Expected behavior:

1. Message sent to Kafka
2. Consumer receives event
3. Critical alert logged
4. Data stored in H2 database

---

# Troubleshooting

## Application keeps restarting

Check:

```bash
docker logs iot-gateway-app
```

---

## Java Version Error

Example:

```
UnsupportedClassVersionError
class file version 65
```

Meaning:

- Application compiled with Java 21
- Runtime was Java 17

Solution:

Use Java 21 Docker image:

```dockerfile
FROM eclipse-temurin:21-jre
```

---

## Kafka Connection Problems

Check:

```bash
docker logs kafka
```

Verify:

```
bootstrap.servers=kafka:9092
```

inside the Spring container.

---

# Future Improvements

Possible production enhancements:

- Replace H2 with PostgreSQL
- Add Kafka Schema Registry
- Add MQTT IoT ingestion layer
- Add authentication with OAuth2/JWT
- Add Prometheus monitoring
- Add Grafana dashboards
- Add Kubernetes deployment
- Add CI/CD pipeline

---

# Author

Enterprise IoT Kafka Gateway Project

Built using:

- Spring Boot
- Apache Kafka
- Docker
- Terraform

