package com.quiz_service_microservice.Quiz_Service_MicroService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class QuizServiceMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizServiceMicroServiceApplication.class, args);
	}

}
