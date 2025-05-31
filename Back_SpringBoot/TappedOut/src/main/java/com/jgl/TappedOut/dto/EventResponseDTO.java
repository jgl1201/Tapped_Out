package com.jgl.TappedOut.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response DTO for an event
 * 
 * @author Jorge García López
 * @version 1.1
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private Long id;

    private UserResponseDTO organizer;

    private SportResponseDTO sport;

    private String name;

    private String description;
    
    private LocalDateTime startDate;

    private LocalDateTime endDate;
    
    private String status;
    
    private String country;

    private String city;

    private String address;

    private String logo;

    private LocalDateTime createdAt;

    private BigDecimal registrationFee;
}