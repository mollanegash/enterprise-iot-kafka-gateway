package com.example.enterprise_iot_kafka_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class EnterpriseIotKafkaGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnterpriseIotKafkaGatewayApplication.class, args);
	}

}
