package com.service.desk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.service.desk.repository"})
@ComponentScan(basePackages = {"com.*"})
@EntityScan(basePackages = {"com.service.desk.entidade"})
@SpringBootApplication
public class ServiceDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDeskApplication.class, args);
	}

}
