package com.example.enterprise_iot_kafka_gateway.repository;

import com.example.enterprise_iot_kafka_gateway.model.SensorTelemetryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<SensorTelemetryEntity, Long> {
}