package fr.celestialdreams.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "fr.celestialdreams.api")
public class ApiCdApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCdApplication.class, args);
	}

}
