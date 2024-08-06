package com.gomez.docutrack.bundle.config;

import com.gomez.docutrack.bundle.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Consider enabling CSRF protection for production
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/login", "/register", "/h2-console/**", "/error").permitAll()
                .requestMatchers("/main").hasAnyRole("USER", "ADMIN")  // Ensure roles in database do not include 'ROLE_' prefix
                .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/documents").permitAll()
                .requestMatchers("/api/documents/update").permitAll()
                .requestMatchers("/api/documents/**").permitAll()
                .requestMatchers("/header").permitAll()
                .requestMatchers("/uploads").permitAll()
                .requestMatchers("/static/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/main", true)
                .failureHandler(customAuthenticationFailureHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            )
            .httpBasic(httpBasic -> {});

        // Allowing H2 console frame access
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
