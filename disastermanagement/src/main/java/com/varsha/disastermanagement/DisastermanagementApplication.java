package com.varsha.disastermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DisastermanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisastermanagementApplication.class, args);
	}

}
