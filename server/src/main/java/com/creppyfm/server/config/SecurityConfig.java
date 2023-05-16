package com.creppyfm.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //delete below line (.csrf().disable() when not testing
                .csrf().disable()
                .authorizeHttpRequests( auth -> {
                    //remove '**' to resume proper filtering (testing)
                    auth.requestMatchers("/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                //.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/login/oauth2/authorization")))
                .formLogin(withDefaults())
                .build();
    }


}
