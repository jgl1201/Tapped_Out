package com.jgl.TappedOut.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration conf) throws Exception {
        return conf.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // ? ========================
                // ? PUBLIC ENDPOINTS
                // ? ========================
                
                // * Event read for non registered users
                .requestMatchers(HttpMethod.GET, "/api/tappedout/event").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tappedout/event/").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tappedout/sport/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tappedout/upcoming/**").permitAll()

                // * User creation (Registering)
                .requestMatchers(HttpMethod.POST, "/api/tappedout/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/tappedout/user/").permitAll()

                // * Auth endpoints
                .requestMatchers("/api/tappedout/auth/login").permitAll()
                .requestMatchers("/api/tappedout/auth/login/").permitAll()
                .requestMatchers("/api/tappedout/auth/register").permitAll()
                .requestMatchers("/api/tappedout/auth/register/").permitAll()
                .requestMatchers("/api/tappedout/auth/validate").permitAll()
                .requestMatchers("/api/tappedout/auth/validate/").permitAll()

                // ? ========================
                // ? GENDER ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/api/tappedout/gender/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Write - ADMIN
                .requestMatchers(HttpMethod.POST, "/api/tappedout/gender/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tappedout/gender/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tappedout/gender/**").hasRole("ADMIN")

                // ? ========================
                // ? SPORT ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/api/tappedout/sport/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Write - ADMIN
                .requestMatchers(HttpMethod.POST, "/api/tappedout/sport/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tappedout/sport/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tappedout/sport/**").hasRole("ADMIN")

                // ? ========================
                // ? USER TYPE ENDPOINTS
                // ? ========================

                // * Only ADMIN has access
                .requestMatchers("/api/tappedout/user-type/**").hasRole("ADMIN")

                // ? ========================
                // ? SPORT LEVEL ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/api/tappedout/sport-level/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Write - ADMIN
                .requestMatchers(HttpMethod.POST, "/api/tappedout/sport-level/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tappedout/sport-level/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tappedout/sport-level/**").hasRole("ADMIN")

                // ? ========================
                // ? USER ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/api/tappedout/user").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/api/tappedout/user/").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/api/tappedout/user/type/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/tappedout/user/gender/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/api/tappedout/user/location/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/tappedout/user/search/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/api/tappedout/user/id/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/api/tappedout/user/dni/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/tappedout/user/email/**").hasRole("ADMIN")

                // * Creation - defined as public endpoint

                // * Update - Requires specific logic
                // * ADMIN: can edit any user
                // * ORGANIZER: can edit their own user
                // * COMPETITOR: can edit their own user
                .requestMatchers(HttpMethod.PUT, "/api/tappedout/user/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.PATCH, "/api/tappedout/user/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Deletion - ADMIN
                .requestMatchers(HttpMethod.DELETE, "/api/tappedout/user/**").hasRole("ADMIN")

                // ? ========================
                // ? EVENT ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/api/tappedout/event/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Creation - ADMIN, ORGANIZER
                .requestMatchers(HttpMethod.POST, "/api/tappedout/event/**").hasAnyRole("ADMIN", "ORGANIZER")

                // * Update / Deletion - Requires specific logic
                // * ADMIN: can edit any event
                // * ORGANIZER: can edit their own event
                .requestMatchers(HttpMethod.PUT, "/api/tappedout/event/**").hasAnyRole("ADMIN", "ORGANIZER")
                .requestMatchers(HttpMethod.DELETE, "/api/tappedout/event/**").hasAnyRole("ADMIN", "ORGANIZER")

                // ? ========================
                // ? CATEGORY ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/api/tappedout/category/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Write - ADMIN
                .requestMatchers(HttpMethod.POST, "/api/tappedout/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tappedout/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tappedout/category/**").hasRole("ADMIN")

                // ? ========================
                // ? INSCRIPTION ENDPOINTS
                // ? ========================

                // * Read (ALL) - ADMIN
                .requestMatchers(HttpMethod.GET, "/api/tappedout/inscription").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/tappedout/inscription/").hasRole("ADMIN")

                // * Read (SPECIFIC) - Requires specific logic
                // * ADMIN: can see all inscriptions
                // * ORGANIZER: can see inscriptions at their events
                // * COMPETITOR: can see their own inscriptions
                .requestMatchers(HttpMethod.GET, "/api/tappedout/inscription/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Creation - ADMIN, COMPETITOR
                .requestMatchers(HttpMethod.POST, "/api/tappedout/inscription/**").hasAnyRole("ADMIN", "COMPETITOR")

                // * Update - Requires specific logic
                // * ADMIN: can edit any inscription
                // * COMPETITOR: can edit their own inscriptions
                .requestMatchers(HttpMethod.PUT, "/api/tappedout/inscription/**").hasAnyRole("ADMIN", "COMPETITOR")

                // * Deletion - Requires specific logic
                // * ADMIN: can delete any inscription
                // * COMPETITOR: can delete their own inscriptions
                .requestMatchers(HttpMethod.DELETE, "/api/tappedout/inscription/**").hasAnyRole("ADMIN", "COMPETITOR")

                // ? ========================
                // ? RESULT ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/api/tappedout/result/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Creation / Update - Requires specific logic
                // * ADMIN: can create/edit any result
                // * ORGANIZER: can create/edit their results at their event
                .requestMatchers(HttpMethod.POST, "/api/tappedout/result/**").hasAnyRole("ADMIN", "ORGANIZER")
                .requestMatchers(HttpMethod.PUT, "/api/tappedout/result/**").hasAnyRole("ADMIN", "ORGANIZER")

                // * Deletion - ADMIN
                .requestMatchers(HttpMethod.DELETE, "/api/tappedout/result/**").hasRole("ADMIN")

                // ? ========================
                // ? ANY OTHER PETITION
                // ? ======================== 
                .anyRequest().authenticated()
            );
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}