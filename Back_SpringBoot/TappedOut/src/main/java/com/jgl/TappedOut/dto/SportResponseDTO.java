package com.jgl.TappedOut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response DTO for a sport
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportResponseDTO {
    private Long id;

    private String name;
}