package com.collegeerp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customConfig(){
        String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("College ERP Backend")
                        .version("v1.0")
                        .description("This is the college ERP backend where the we can add Teachers, Student, Subjects, Branch and All the teachers can mark the attendance")
                        .contact(new Contact()
                                .name("Ronit Verma")
                                .email("ronitroy22678@gmail.com")
                                .url("http://ronit.com"))
                );
    }
}
