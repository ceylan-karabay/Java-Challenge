package org.example;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
        info = @Info(
                title = "Twitter Clone API",
                version = "1.0",
                description = "Twitter Backend REST API Dokümantasyonu"
        )
)
@SpringBootApplication
public class TwitterApplication {
    public static void main(String[] args) {
        SpringApplication.run(TwitterApplication.class, args);
    }
}
