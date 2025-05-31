package com.jgl.TappedOut.dto;

import com.jgl.TappedOut.models.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for updating a inscription in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionUpdateDTO {
    @NotNull(message = "Can't update an Inscription without Category")
    private Long categoryId;

    @NotNull(message = "Can't update an Inscription without Payment Status")
    private PaymentStatus paymentStatus;
}