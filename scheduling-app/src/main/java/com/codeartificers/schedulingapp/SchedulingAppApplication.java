package com.codeartificers.schedulingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SchedulingAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(SchedulingAppApplication.class, args);
	}

	@GetMapping("/")
	public String APIroot(){
		return "Hello World";
	}
}
