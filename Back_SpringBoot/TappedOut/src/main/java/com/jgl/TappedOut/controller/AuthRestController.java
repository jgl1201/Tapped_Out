package com.jgl.TappedOut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jgl.TappedOut.conf.JwtTokenProvider;
import com.jgl.TappedOut.dto.LoginRequestDTO;
import com.jgl.TappedOut.dto.LoginResponseDTO;
import com.jgl.TappedOut.dto.RegisterRequestDTO;
import com.jgl.TappedOut.models.User;
import com.jgl.TappedOut.repositories.GenderRepository;
import com.jgl.TappedOut.repositories.UserRepository;
import com.jgl.TappedOut.repositories.UserTypeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

/**
 * Controller to define endpoints for Auth
 * 
 * @author Jorge García Lopez
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private GenderRepository genderRepo;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint to login
     * POST /auth/login
     */
    @PostMapping({"/login", "/login/"})
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            
            UserDetails details = (UserDetails) auth.getPrincipal();
            String token = jwtTokenProvider.generateToken(details.getUsername());

            User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(token);
            response.setTokenType("Bearer");
            response.setUserId(user.getId());
            response.setEmail(user.getEmail().trim().toLowerCase());
            response.setFirstName(user.getFirstName().trim());
            response.setLastName(user.getLastName().trim());
            response.setUserType(user.getTypeId().getName().trim().toUpperCase());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    /**
     * Endpoint to register
     * POST /auth/register
     */
    @PostMapping({"/register", "/register/"})
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO dto) {
        try {
            if (userRepository.existsByEmailIgnoreCase(dto.getEmail().trim().toLowerCase()))
                return ResponseEntity.status(400).body("Email already exists");

            if (userRepository.existsByDniIgnoreCase(dto.getDni().trim().toUpperCase()))
                return ResponseEntity.status(400).body("DNI already exists");

            String userType = dto.getUserType().trim().toUpperCase() != null ?
                dto.getUserType().trim().toUpperCase() : "COMPETITOR"; // Default user type is competitor

            if (!"COMPETITOR".equals(userType) && !"ORGANIZER".equals(userType))
                return ResponseEntity.status(400).body("That user type needs to be registered by a ADMIN");

            User user = new User();
            user.setEmail(dto.getEmail().trim().toLowerCase());
            user.setPasswordHash(passwordEncoder.encode(dto.getPassword().trim()));
            user.setDni(dto.getDni().trim().toUpperCase());
            user.setFirstName(dto.getFirstName().trim());
            user.setLastName(dto.getLastName().trim());
            user.setDateOfBirth(dto.getDateOfBirth());
            user.setCountry(dto.getCountry().trim().toUpperCase());
            user.setCity(dto.getCity().trim().toUpperCase());
            user.setTypeId(
                userTypeRepository.findByName(userType).orElseThrow(() -> new RuntimeException("User type not found"))
            );

            if (dto.getGenderId() != null) {
                user.setGenderId(
                    genderRepo.findById(dto.getGenderId())
                        .orElseThrow(() -> new RuntimeException("Gender not found"))
                );
            }

            User saved = userRepository.save(user);

            String token = jwtTokenProvider.generateToken(saved.getEmail().trim().toLowerCase());

            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(token);
            response.setTokenType("Bearer");
            response.setUserId(saved.getId());
            response.setEmail(saved.getEmail().trim().toLowerCase());
            response.setFirstName(saved.getFirstName().trim());
            response.setLastName(saved.getLastName().trim());
            response.setUserType(saved.getTypeId().getName().trim().toUpperCase());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    /**
     * Endpoint to validate token
     * GET /auth/validate
     */
    @GetMapping({"/validate", "/validate/"})
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            String token = authHeader.substring(7);
            
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            String email = jwtTokenProvider.getUsernameFromToken(token);
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

            TokenValidationResponse response = new TokenValidationResponse();
            response.setValid(true);
            response.setUserId(user.getId());
            response.setEmail(user.getEmail());
            response.setUserType(user.getTypeId().getName());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }


    public static class TokenValidationResponse {
        private boolean valid;
        private Long userId;
        private String email;
        private String userType;

        // Getters y setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
    }
}
