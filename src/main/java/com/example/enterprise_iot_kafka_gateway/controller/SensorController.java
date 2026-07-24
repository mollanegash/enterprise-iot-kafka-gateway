package com.example.enterprise_iot_kafka_gateway.controller;

import com.example.enterprise_iot_kafka_gateway.model.SensorTelemetryEntity;
import com.example.enterprise_iot_kafka_gateway.repository.SensorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
public class SensorController {

    private final SensorRepository sensorRepository;

    public SensorController(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @GetMapping
    public ResponseEntity<List<SensorTelemetryEntity>> getAllTelemetry() {
        List<SensorTelemetryEntity> logs = sensorRepository.findAll();
        return ResponseEntity.ok(logs);
    }
}