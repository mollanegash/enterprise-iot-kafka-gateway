package com.example.enterprise_iot_kafka_gateway.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "sensor_telemetry_logs")
public class SensorTelemetryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double temperature;

    @Column(nullable = false)
    private double pressure;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    public SensorTelemetryEntity() {}

    public SensorTelemetryEntity(String deviceId, String location, double temperature, double pressure, String status, OffsetDateTime timestamp) {
        this.deviceId = deviceId;
        this.location = location;
        this.temperature = temperature;
        this.pressure = pressure;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public String getLocation() { return location; }
    public double getTemperature() { return temperature; }
    public double getPressure() { return pressure; }
    public String getStatus() { return status; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}