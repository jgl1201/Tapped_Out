package com.jgl.TappedOut.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response DTO for a category
 * 
 * @author Jorge García López
 * @version 1.1
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    
    private String name;

    private Integer minAge;
    
    private Integer maxAge;
    
    private BigDecimal minWeight;
    
    private BigDecimal maxWeight;

    private SportResponseDTO sport;
    
    private GenderResponseDTO gender;
    
    private SportLevelResponseDTO level;
}