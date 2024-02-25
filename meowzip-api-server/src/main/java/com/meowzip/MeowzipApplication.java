package com.meowzip;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableCaching
@EnableFeignClients
@OpenAPIDefinition(servers = {@Server(url = "https://dev-server.meowzip.com", description = "개발 서버")})
@SpringBootApplication
public class MeowzipApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeowzipApplication.class, args);
	}

}
