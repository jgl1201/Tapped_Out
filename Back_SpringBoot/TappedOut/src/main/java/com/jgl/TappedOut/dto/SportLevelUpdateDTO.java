package com.jgl.TappedOut.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for updating a sport level in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportLevelUpdateDTO {
    @NotBlank(message = "Can't update a Sport Level without Name")
    @Size(max = 100, message = "Sport Level's name's too long")
    private String name;

    @NotNull(message = "Can't update a Sport Level without Sport")
    private Long sportId;
}