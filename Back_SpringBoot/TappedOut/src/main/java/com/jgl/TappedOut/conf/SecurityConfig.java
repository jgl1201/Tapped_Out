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
                .requestMatchers(HttpMethod.GET, "/event").permitAll()
                .requestMatchers(HttpMethod.GET, "/event/").permitAll()
                .requestMatchers(HttpMethod.GET, "/sport/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/upcoming/**").permitAll()

                // * User creation (Registering)
                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/").permitAll()

                // * Auth endpoints
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/login/").permitAll()
                .requestMatchers("/auth/register").permitAll()
                .requestMatchers("/auth/register/").permitAll()
                .requestMatchers("/auth/validate").permitAll()
                .requestMatchers("/auth/validate/").permitAll()

                // ? ========================
                // ? GENDER ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/gender/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Write - ADMIN
                .requestMatchers(HttpMethod.POST, "/gender/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/gender/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/gender/**").hasRole("ADMIN")

                // ? ========================
                // ? SPORT ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/sport/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Write - ADMIN
                .requestMatchers(HttpMethod.POST, "/sport/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/sport/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/sport/**").hasRole("ADMIN")

                // ? ========================
                // ? USER TYPE ENDPOINTS
                // ? ========================

                // * Only ADMIN has access
                .requestMatchers("/user-type/**").hasRole("ADMIN")

                // ? ========================
                // ? SPORT LEVEL ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/sport-level/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Write - ADMIN
                .requestMatchers(HttpMethod.POST, "/sport-level/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/sport-level/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/sport-level/**").hasRole("ADMIN")

                // ? ========================
                // ? USER ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/user").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/user/").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/user/type/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/gender/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/user/location/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/search/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/user/id/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.GET, "/user/dni/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/email/**").hasRole("ADMIN")

                // * Creation - defined as public endpoint

                // * Update - Requires specific logic
                // * ADMIN: can edit any user
                // * ORGANIZER: can edit their own user
                // * COMPETITOR: can edit their own user
                .requestMatchers(HttpMethod.PUT, "/user/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")
                .requestMatchers(HttpMethod.PATCH, "/user/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Deletion - ADMIN
                .requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")

                // ? ========================
                // ? EVENT ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/event/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Creation - ADMIN, ORGANIZER
                .requestMatchers(HttpMethod.POST, "/event/**").hasAnyRole("ADMIN", "ORGANIZER")

                // * Update / Deletion - Requires specific logic
                // * ADMIN: can edit any event
                // * ORGANIZER: can edit their own event
                .requestMatchers(HttpMethod.PUT, "/event/**").hasAnyRole("ADMIN", "ORGANIZER")
                .requestMatchers(HttpMethod.DELETE, "/event/**").hasAnyRole("ADMIN", "ORGANIZER")

                // ? ========================
                // ? CATEGORY ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/category/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Write - ADMIN
                .requestMatchers(HttpMethod.POST, "/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/category/**").hasRole("ADMIN")

                // ? ========================
                // ? INSCRIPTION ENDPOINTS
                // ? ========================

                // * Read (ALL) - ADMIN
                .requestMatchers(HttpMethod.GET, "/inscription").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/inscription/").hasRole("ADMIN")

                // * Read (SPECIFIC) - Requires specific logic
                // * ADMIN: can see all inscriptions
                // * ORGANIZER: can see inscriptions at their events
                // * COMPETITOR: can see their own inscriptions
                .requestMatchers(HttpMethod.GET, "/inscription/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Creation - ADMIN, COMPETITOR
                .requestMatchers(HttpMethod.POST, "/inscription/**").hasAnyRole("ADMIN", "COMPETITOR")

                // * Update - Requires specific logic
                // * ADMIN: can edit any inscription
                // * COMPETITOR: can edit their own inscriptions
                .requestMatchers(HttpMethod.PUT, "/inscription/**").hasAnyRole("ADMIN", "COMPETITOR")

                // * Deletion - Requires specific logic
                // * ADMIN: can delete any inscription
                // * COMPETITOR: can delete their own inscriptions
                .requestMatchers(HttpMethod.DELETE, "/inscription/**").hasAnyRole("ADMIN", "COMPETITOR")

                // ? ========================
                // ? RESULT ENDPOINTS
                // ? ========================

                // * Read - ADMIN, ORGANIZER, COMPETITOR
                .requestMatchers(HttpMethod.GET, "/result/**").hasAnyRole("ADMIN", "ORGANIZER", "COMPETITOR")

                // * Creation / Update - Requires specific logic
                // * ADMIN: can create/edit any result
                // * ORGANIZER: can create/edit their results at their event
                .requestMatchers(HttpMethod.POST, "/result/**").hasAnyRole("ADMIN", "ORGANIZER")
                .requestMatchers(HttpMethod.PUT, "/result/**").hasAnyRole("ADMIN", "ORGANIZER")

                // * Deletion - ADMIN
                .requestMatchers(HttpMethod.DELETE, "/result/**").hasRole("ADMIN")

                // ? ========================
                // ? ANY OTHER PETITION
                // ? ======================== 
                .anyRequest().authenticated()
            );
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}