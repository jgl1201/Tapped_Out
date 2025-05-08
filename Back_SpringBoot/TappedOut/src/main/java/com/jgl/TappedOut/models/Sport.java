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
 * Represents a sport discipline in the system
 * Each sport canhave multiple categories and levels associated with it
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "sports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Sport NAME cannot be NULL")
    @NotEmpty(message = "Sport NAME cannot be EMPTY")
    @NotBlank(message = "Sport NAME cannot be BLANK")
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
}