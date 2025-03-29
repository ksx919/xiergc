package com.example.xiergc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.example.xiergc.mapper")
public class XiergcApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiergcApplication.class, args);
	}

}
