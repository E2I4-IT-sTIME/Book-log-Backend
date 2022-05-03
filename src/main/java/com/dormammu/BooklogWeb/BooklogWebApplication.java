package com.dormammu.BooklogWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BooklogWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooklogWebApplication.class, args);
		
	}
}
