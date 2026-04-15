package com.cognizant.tms.api_gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiGatewayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Training Management System - API Gateway")
                        .description("Aggregated API documentation for all microservices. " +
                                "Use the dropdown in the top bar to switch between services.")
                        .version("1.0.0"));
    }
}

