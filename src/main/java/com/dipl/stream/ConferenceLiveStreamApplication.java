package com.dipl.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ConferenceLiveStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceLiveStreamApplication.class, args);
	}

}
