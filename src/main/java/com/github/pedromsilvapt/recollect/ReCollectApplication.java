package com.github.pedromsilvapt.recollect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ReCollectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReCollectApplication.class, args);
	}

	@GetMapping("/hello")
	public String index () {
		return "Hello World!";
	}

}
