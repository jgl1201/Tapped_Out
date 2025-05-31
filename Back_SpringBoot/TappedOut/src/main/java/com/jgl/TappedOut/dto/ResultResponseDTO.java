package com.jgl.TappedOut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response DTO for a result
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponseDTO {
    private Long id;

    private EventResponseDTO event;

    private CategoryResponseDTO category;

    private UserResponseDTO competitor;

    private Integer position;

    private String notes;
}