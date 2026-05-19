package com.OnlineQuizApp.Online_Quiz_Application.config;

import com.OnlineQuizApp.Online_Quiz_Application.service.QuizUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private final QuizUserDetailsService userDetailsService;

    public WebSecurityConfig(
            QuizUserDetailsService userDetailsService) {

        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // Public pages
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/css/**"
                        ).permitAll()

                        // Admin only
                        .requestMatchers(
                                "/QuizList",
                                "/addQuiz/**",
                                "/editQuiz/**",
                                "/deleteQuestion/**"
                        ).hasRole("ADMIN")

                        // User only
                        .requestMatchers(
                                "/Quiz",
                                "/submitQuiz"
                        ).hasRole("USER")

                        // All other requests need authentication
                        .anyRequest().authenticated()
                )

                // Login configuration
                .formLogin(login -> login

                        .loginPage("/login")

                        .defaultSuccessUrl("/home", true)

                        .permitAll()
                )

                // Logout configuration
                .logout(logout -> logout

                        .logoutSuccessUrl("/login?logout")

                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }
}