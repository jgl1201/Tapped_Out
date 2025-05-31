package com.jgl.TappedOut.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response DTO for an user
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;

    private String dni;

    private UserTypeResponseDTO type;

    private String email;

    private String passwordHash;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private GenderResponseDTO genderId;

    private String country;

    private String city;

    private Integer phone;

    private String avatar;

    private Boolean isVerified;

    private LocalDateTime createdAt;
}