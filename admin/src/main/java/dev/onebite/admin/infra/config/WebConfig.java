package dev.onebite.admin.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://127.0.0.1:3000",
                        "http://localhost:8080",
                        "http://127.0.0.1:8080",
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "https://dev.devonebite.xyz",
                        "https://devonebite.xyz",
                        "https://api.devonebite.xyz/dev",
                        "https://api.devonebite.xyz/prod",
                        "https://api.devonebite.xyz",
                        "https://dev.admin.devonebite.xyz/",
                        "https://dev.devonebite.xyz/",
                        "https://prod.admin.devonebite.xyz/",
                        "https://devonebite.xyz/",
                        "http://localhost:3001/"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}