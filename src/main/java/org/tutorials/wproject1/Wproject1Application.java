package org.tutorials.wproject1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Wproject1Application {
	private static final Logger LOGGER=LoggerFactory.getLogger(Wproject1Application.class);
 
	public static void main(String[] args) {
		SpringApplication.run(Wproject1Application.class, args);
		LOGGER.info("Wproject1Application  {}", "for WH");
	}

}
