package com.meowzip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class MeowzipApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeowzipApplication.class, args);
	}

}
