package com.akm.messagescheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "UseCase Implementation APIs"))
public class MessageSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageSchedulerApplication.class, args);
	}

}
