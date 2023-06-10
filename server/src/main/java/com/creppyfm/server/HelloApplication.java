package com.creppyfm.server;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.SessionTrackingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args)

    {
        Dotenv dotenv = Dotenv.load();
        // MongoDB Credentials
        System.setProperty("MONGO_DATABASE", System.getenv("MONGO_DATABASE"));
        System.setProperty("MONGO_URI", System.getenv("MONGO_URI"));
        // OpenAI Credentials
        System.setProperty("OPENAI_API_KEY", System.getenv("OPENAI_API_KEY"));
        // Google OAuth2 Credentials
        System.setProperty("GOOGLE_CLIENT_ID", System.getenv("GOOGLE_CLIENT_ID"));
        System.setProperty("GOOGLE_CLIENT_SECRET", System.getenv("GOOGLE_CLIENT_SECRET"));
        // GitHub OAuth2 Credentials
        System.setProperty("GITHUB_CLIENT_ID", System.getenv("GITHUB_CLIENT_ID"));
        System.setProperty("GITHUB_CLIENT_SECRET", System.getenv("GITHUB_CLIENT_SECRET"));

        SpringApplication.run(HelloApplication.class, args);
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> servletContext.setSessionTrackingModes(
                Set.of(SessionTrackingMode.COOKIE));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        /*
                        * Modify to include deployed client and server urls
                        * */
                        .allowedOrigins("http://localhost:5173", "http://localhost:8080", "https://creppyfm.github.io/DeleGate/", "https://delegate.herokuapp.com/")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true).maxAge(3600);
            }
        };
    }

}