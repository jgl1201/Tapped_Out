package com.jgl.TappedOut.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for creating a new result in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultCreateDTO {
    @NotNull(message = "Can't create a Result without Event")
    private Long eventId;

    @NotNull(message = "Can't create a Result without Category")
    private Long categoryId;

    @NotNull(message = "Can't create a Result without Competitor")
    private Long competitorId;

    @NotNull(message = "Can't create a Result without Position")
    @Positive(message = "Position can't be negative")
    private Integer position;

    @Size(max = 1000, message = "Note's too long")
    private String notes;
}