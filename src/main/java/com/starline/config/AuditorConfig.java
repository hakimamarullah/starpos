package com.starline.config;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/7/2024 12:34 PM
@Last Modified 11/7/2024 12:34 PM
Version 1.0
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "dateTimeProvider")
public class AuditorConfig {

    @Bean
    AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!Objects.isNull(authentication) && authentication.getPrincipal() instanceof UserDetails user) {
                return Optional.of(user.getUsername());
            }
            return Optional.of("SYSTEM");

        };
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now());
    }
}
