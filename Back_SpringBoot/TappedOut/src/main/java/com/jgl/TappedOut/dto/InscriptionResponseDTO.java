package com.jgl.TappedOut.dto;

import java.time.LocalDateTime;

import com.jgl.TappedOut.models.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response DTO for a inscription
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionResponseDTO {
    private Long id;

    private UserResponseDTO competitor;

    private EventResponseDTO event;

    private CategoryResponseDTO category;

    private LocalDateTime registerDate;

    private PaymentStatus paymentStatus;
}