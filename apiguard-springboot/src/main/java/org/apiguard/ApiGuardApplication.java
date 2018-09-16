package org.apiguard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApiGuardApplication {

	private static final Logger logger = LoggerFactory.getLogger(ApiGuardApplication.class);

	public static void main(String[] args) {

		logger.debug("--Application Starting --");
		ConfigurableApplicationContext run = SpringApplication.run(ApiGuardApplication.class, args);
		logger.debug("--Application Started --");
	}

}
