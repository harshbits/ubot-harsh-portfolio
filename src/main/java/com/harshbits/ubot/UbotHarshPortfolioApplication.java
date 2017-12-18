package com.harshbits.ubot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableConfigurationProperties
public class UbotHarshPortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(UbotHarshPortfolioApplication.class, args);
	}
}
