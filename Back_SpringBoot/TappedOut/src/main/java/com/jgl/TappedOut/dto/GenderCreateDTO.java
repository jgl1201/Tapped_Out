package com.jgl.TappedOut.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for creating a new gender in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderCreateDTO {
    @NotBlank(message = "Can't create a Gender without Name")
    @Size(max = 50, message = "Gender's name's too long")
    private String name;
}