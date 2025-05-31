package com.jgl.TappedOut.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for updating a category in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDTO {
    @NotBlank(message = "Can't update a Category without Name")
    private String name;

    private Integer minAge;
    
    private Integer maxAge;
    
    private BigDecimal minWeight;
    
    private BigDecimal maxWeight;

    private Long levelId;
}