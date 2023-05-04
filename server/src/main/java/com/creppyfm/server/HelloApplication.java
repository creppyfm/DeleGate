package com.creppyfm.server;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args)

    {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("MONGO_USER", dotenv.get("MONGO_USER"));
        System.setProperty("MONGO_PASSWORD", dotenv.get("MONGO_PASSWORD"));
        System.setProperty("MONGO_CLUSTER", dotenv.get("MONGO_CLUSTER"));
        System.setProperty("MONGO_DATABASE", dotenv.get("MONGO_DATABASE"));
        System.setProperty("OPENAI_API_KEY", dotenv.get("OPENAI_API_KEY"));

        //TODO: remove test print statement below
        System.out.println(dotenv.get("MONGO_USER"));

        SpringApplication.run(HelloApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(false).maxAge(3600);
            }
        };
    }

}