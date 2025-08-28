package com.hra.hra.config;

import com.hra.hra.dto.ProjectDto;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.Project;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
