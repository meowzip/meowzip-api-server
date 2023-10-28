package com.meowzip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MeowzipApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeowzipApplication.class, args);
	}

}
