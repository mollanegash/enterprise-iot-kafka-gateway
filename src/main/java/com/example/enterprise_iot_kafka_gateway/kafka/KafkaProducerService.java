package com.example.enterprise_iot_kafka_gateway.producer;

import com.example.enterprise_iot_kafka_gateway.model.SensorTelemetry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "iot-telemetry-stream";
    private final KafkaTemplate<String, SensorTelemetry> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, SensorTelemetry> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTelemetry(SensorTelemetry telemetry) {
        kafkaTemplate.send(TOPIC, telemetry.getDeviceId(), telemetry);
    }
}