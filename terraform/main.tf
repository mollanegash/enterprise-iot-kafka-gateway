terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.1"
    }
  }
}

provider "docker" {
  host = "unix:///var/run/docker.sock"
}

# 1. Build Spring Boot Docker Image
resource "null_resource" "build_app_image" {
  provisioner "local-exec" {
    command = "docker build -t enterprise-iot-kafka-gateway -f ${abspath("${path.root}/../Dockerfile")} ${abspath("${path.root}/..")}"
  }
}

# 2. Dedicated Docker Network
resource "docker_network" "iot_network" {
  name   = "iot-kafka-net"
  driver = "bridge"
}

# 3. ZooKeeper
resource "docker_container" "zookeeper" {
  image = "confluentinc/cp-zookeeper:7.5.0"
  name  = "zookeeper"

  network_mode = docker_network.iot_network.name

  restart = "always"

  env = [
    "ZOOKEEPER_CLIENT_PORT=2181",
    "ZOOKEEPER_TICK_TIME=2000"
  ]
}


# 4. Kafka Broker
resource "docker_container" "kafka" {
  image = "confluentinc/cp-kafka:7.5.0"
  name  = "kafka"

  network_mode = docker_network.iot_network.name

  restart = "always"

  ports {
    internal = 9092
    external = 9092
  }

  env = [
    "KAFKA_BROKER_ID=1",

    "KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181",

    # Listener security mapping
    "KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT",

    # Kafka listens internally and externally
    "KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092,PLAINTEXT_HOST://0.0.0.0:29092",

    # Internal Docker communication + host access
    "KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092",

    "KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1",

    "KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1",
    "KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1"
  ]

  depends_on = [
    docker_container.zookeeper
  ]
}


# 5. Spring Boot IoT Gateway
resource "docker_container" "iot_gateway" {

  image = "enterprise-iot-kafka-gateway"
  name  = "iot-gateway-app"

  network_mode = docker_network.iot_network.name

  restart = "always"

  ports {
    internal = 8080
    external = 8080
  }

  env = [
    "SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092"
  ]

  depends_on = [
    null_resource.build_app_image,
    docker_container.kafka
  ]
}
