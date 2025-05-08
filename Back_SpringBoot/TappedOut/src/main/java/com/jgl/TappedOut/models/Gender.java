package com.jgl.TappedOut.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the gender entities in the system
 * Used to categorized users and competition categories
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
*/
@Entity
@Table(name="genders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Gender NAME cannot be NULL")
    @NotEmpty(message = "Gender NAME cannot be EMPTY")
    @NotBlank(message = "Gender NAME  cannot be BLANK")
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;
}