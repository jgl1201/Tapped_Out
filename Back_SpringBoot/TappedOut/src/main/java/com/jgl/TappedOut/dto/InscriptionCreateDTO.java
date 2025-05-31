package com.jgl.TappedOut.dto;

import com.jgl.TappedOut.models.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for creating a new inscription in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionCreateDTO {
    @NotNull(message = "Can't create an Inscription without Competitor")
    private Long competitorId;

    @NotNull(message = "Can't create an Inscription without Event")
    private Long eventId;

    @NotNull(message = "Can't create an Inscription without Category")
    private Long categoryId;

    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
}