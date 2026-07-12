package net.number33.warchest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WarchestApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarchestApplication.class, args);
	}

}
