package com.hra.hra.config;

import com.hra.hra.dto.ProjectDto;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.EmployeeRole;
import com.hra.hra.entity.Project;
import com.hra.hra.repository.EmployeeRoleRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
       return new ModelMapper();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management System - API")
                        .version("1.0")
                        .description("API documentation for Employee Management System")
                        .contact(new Contact().email("gaurav@gmail.com").name("Kumar").url("abc@gmail.com")));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
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
