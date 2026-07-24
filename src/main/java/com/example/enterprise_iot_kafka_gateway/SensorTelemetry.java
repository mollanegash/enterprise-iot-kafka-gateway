package com.example.enterprise_iot_kafka_gateway.model;

import java.time.OffsetDateTime;

public class SensorTelemetry {
    private String deviceId;
    private String location;
    private double temperature;
    private double pressure;
    private String status;
    private OffsetDateTime timestamp;

    public SensorTelemetry() {}

    public SensorTelemetry(String deviceId, String location, double temperature, double pressure, String status, OffsetDateTime timestamp) {
        this.deviceId = deviceId;
        this.location = location;
        this.temperature = temperature;
        this.pressure = pressure;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
}