package com.jgl.TappedOut.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for creating a new category in the system
 * 
 * @author Jorge García López
 * @version 1.1
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDTO {
    @NotNull(message = "Can't create a Category without Sport")
    private Long sportId;

    @NotBlank(message = "Can't create a Category without Name")
    @Size(max = 100, message = "Category's name's too long")
    private String name;

    private Integer minAge;

    private Integer maxAge;

    private BigDecimal minWeight;

    private BigDecimal maxWeight;

    @NotNull(message = "Can't create a Category without Gender")
    private Long genderId;
    
    private Long levelId;
}