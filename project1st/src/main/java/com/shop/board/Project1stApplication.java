package com.shop.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Project1stApplication {

	public static void main(String[] args) {
		SpringApplication.run(Project1stApplication.class, args);
	}

}
