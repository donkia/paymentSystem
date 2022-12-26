package com.kakaoinsurance.payment.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi paymentAPI() {
        return GroupedOpenApi.builder()
                .group("payment system")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI paymentOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Spring Boot API")
                        .description("결제시스템")
                        .version("v1.0.0"));
    }
}
