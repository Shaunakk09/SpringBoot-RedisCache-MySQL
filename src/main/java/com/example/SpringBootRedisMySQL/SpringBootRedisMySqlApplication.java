package com.example.SpringBootRedisMySQL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootRedisMySqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisMySqlApplication.class, args);
	}

}
