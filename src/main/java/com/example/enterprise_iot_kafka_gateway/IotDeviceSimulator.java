package com.example.enterprise_iot_kafka_gateway.simulator;

import com.example.enterprise_iot_kafka_gateway.model.SensorTelemetry;
import com.example.enterprise_iot_kafka_gateway.producer.KafkaProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Random;

@Component
public class IotDeviceSimulator {

    private final KafkaProducerService producerService;
    private final Random random = new Random();

    public IotDeviceSimulator(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @Scheduled(fixedRate = 3000)
    public void simulateSensorData() {
        String deviceId = "IOT-DEVICE-" + (random.nextInt(5) + 1);
        String location = "Zone-" + (char) ('A' + random.nextInt(3));
        double temperature = 70.0 + (30.0 * random.nextDouble());
        double pressure = 1000.0 + (50.0 * random.nextDouble());
        String status = temperature > 92.0 ? "CRITICAL" : "NORMAL";

        SensorTelemetry telemetry = new SensorTelemetry(deviceId, location, temperature, pressure, status, OffsetDateTime.now());
        producerService.sendTelemetry(telemetry);
    }
}