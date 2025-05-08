package com.jgl.TappedOut.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the skill level within a specific sport in the system
 * Used to categorize competitors and competition divisions by skill level
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "sport_levels",uniqueConstraints = @UniqueConstraint(columnNames = {"sport_id", "name"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "SportLevel SPORTID cannot be NULL")
    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sportId;

    @NotNull(message = "SportLevel NAME cannot be NULL")
    @NotEmpty(message = "SportLevel NAME cannot be EMPTY")
    @NotBlank(message = "SportLevel NAME cannot be BLANK")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
}