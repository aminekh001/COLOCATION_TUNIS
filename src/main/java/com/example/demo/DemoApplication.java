package com.example.demo;

import com.example.demo.role.Role;
import com.example.demo.role.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}
	@Bean
	public CommandLineRunner runner(RoleRepo roleRepo){
		return  args -> {
			if (roleRepo.findByName("USER").isEmpty()){
				roleRepo.save(
						Role.builder().name("User").build()
				);
			}
		};
	}

}
