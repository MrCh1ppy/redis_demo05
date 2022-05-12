package com.example.redis_demo05;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.redis_demo05.mapper")
public class RedisDemo05Application {

	public static void main(String[] args) {
		SpringApplication.run(RedisDemo05Application.class, args);
	}

}
