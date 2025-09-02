package com.hra.hra;

import com.hra.hra.config.RoleType;
import com.hra.hra.entity.EmployeeRole;
import com.hra.hra.repository.EmployeeRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HraPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HraPortalApplication.class, args);
	}

}
