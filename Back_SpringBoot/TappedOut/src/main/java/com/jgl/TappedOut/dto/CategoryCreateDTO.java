package com.jgl.TappedOut.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for creating a new category in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDTO {
    @NotNull (message = "Category SPORTID cannot be NULL")
    private Long sportId;

    @NotNull (message = "Category NAME cannot be NULL")
    @NotEmpty (message = "Category NAME cannot be EMPTY")
    @NotBlank (message = "Category NAME cannot be BLANK")
    private String name;

    private Integer minAge;
    private Integer maxAge;

    private BigDecimal minWeight;
    private BigDecimal maxWeight;

    @NotNull (message = "Category GENDERID cannot be NULL")
    private Long genderId;
    
    private Long levelId;
}
