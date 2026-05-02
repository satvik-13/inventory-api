package com.satvik.inventory;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory & Order Management API")
                        .version("1.0.0")
                        .description("""
                                A production-grade REST API for retail inventory management.
                                
                                Features:
                                - Full CRUD for Products, Categories, and Orders
                                - Real-time stock tracking with low-stock alerts
                                - Automatic stock deduction on order placement
                                - Stock restoration on order cancellation
                                - Revenue analytics dashboard
                                - Input validation and structured error responses
                                
                                Built with Spring Boot 3, Hibernate ORM, H2/MySQL, and OpenAPI 3.
                                """)
                        .contact(new Contact()
                                .name("Satvik Sarawagi")
                                .email("satviksarawagi05@gmail.com")
                                .url("https://github.com/satvik-13")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development")
                ));
    }
}
