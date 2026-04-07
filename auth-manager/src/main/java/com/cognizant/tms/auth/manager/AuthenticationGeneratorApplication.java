package com.cognizant.tms.auth.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = "com.cognizant.tms.auth.manager")
@PropertySource("classpath:message.properties")
public class AuthenticationGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationGeneratorApplication.class, args);
	}

}
