package com.akm.messagescheduler;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.akm.messagescheduler.utils.ScheduleSend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "UseCase Implementation APIs"))
public class MessageSchedulerApplication {

	@Autowired
	ScheduleSend scheduleSend;

	private final Timer timer = new Timer();

	public static void main(String[] args) {
		SpringApplication.run(MessageSchedulerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void executeTimer() {
		System.out.println("in the timer");
		timer.schedule(scheduleSend, 1000, 3000);
	}

}
