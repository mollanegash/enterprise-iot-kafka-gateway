package com.example.enterprise_iot_kafka_gateway.consumer;

import com.example.enterprise_iot_kafka_gateway.model.SensorTelemetry;
import com.example.enterprise_iot_kafka_gateway.model.SensorTelemetryEntity;
import com.example.enterprise_iot_kafka_gateway.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final SensorRepository sensorRepository;

    public KafkaConsumerService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @KafkaListener(topics = "iot-telemetry-stream", groupId = "iot-gateway-group")
    public void consume(SensorTelemetry telemetry) {
        if ("CRITICAL".equals(telemetry.getStatus())) {
            log.warn("🚨 CRITICAL ALERT! Device {} in {} has exceeded safe temperature limits! Current Temp: {}°C",
                    telemetry.getDeviceId(), telemetry.getLocation(), telemetry.getTemperature());
        }

        SensorTelemetryEntity entity = new SensorTelemetryEntity(
                telemetry.getDeviceId(),
                telemetry.getLocation(),
                telemetry.getTemperature(),
                telemetry.getPressure(),
                telemetry.getStatus(),
                telemetry.getTimestamp()
        );
        sensorRepository.save(entity);
    }
}