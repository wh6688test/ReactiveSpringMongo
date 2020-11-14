package org.tutorials.wproject1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.tutorials.wproject1.config.AppConfig;

@SpringBootApplication
public class Wproject1Application {

	public static void main(String[] args) {
		SpringApplication.run(Wproject1Application.class, args);
	}

}
