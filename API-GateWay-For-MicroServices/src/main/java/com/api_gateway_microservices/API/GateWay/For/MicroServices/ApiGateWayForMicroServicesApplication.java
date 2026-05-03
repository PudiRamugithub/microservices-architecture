package com.api_gateway_microservices.API.GateWay.For.MicroServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class ApiGateWayForMicroServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGateWayForMicroServicesApplication.class, args);
	}

}
