package com.Dashboard_gestao_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class DashboardGestaoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardGestaoApiApplication.class, args);
	}

}
