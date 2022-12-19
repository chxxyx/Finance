package com.chxxyx.dividendproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividendProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(DividendProjectApplication.class, args);
	}

}
