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

	@Bean
	public CommandLineRunner initRoles(EmployeeRoleRepository roleRepo) {
		return args -> {
			try {
				if (roleRepo.count() == 0) {
					List<EmployeeRole> roles = Arrays.stream(RoleType.values())
							.map(EmployeeRole::new)
							.toList();
					roleRepo.saveAll(roles);
					roles.forEach(r -> System.out.println("Inserted: " + r.getName()));
				}
			} catch (Exception e) {
				e.printStackTrace(); // show full error in console
			}
		};
	}

}
