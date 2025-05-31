package com.jgl.TappedOut.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for updating a result in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultUpdateDTO {
    @NotNull(message = "Can't update a Result without Position")
    @Positive(message = "Position can't be negative")
    private Integer position;

    @Size(max = 1000, message = "Note's too long")
    private String notes;
}