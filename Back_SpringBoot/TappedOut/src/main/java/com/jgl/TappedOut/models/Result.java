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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents a competitior's results for a specific event in the system
 * Contains information about the final position and optional notes
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "results", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"event_id", "category_id", "competitor_id"}),
    @UniqueConstraint(columnNames = {"event_id", "category_id", "position"})
})
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Result EVENTID cannot be NULL")
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event eventId;

    @NotNull(message = "Result CATEGORYID cannot be NULL")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category categoryId;

    @NotNull(message = "Result COMPETITORID cannot be NULL")
    @ManyToOne
    @JoinColumn(name = "competitor_id", nullable = false)
    private User competitorId;

    @NotNull(message = "Result POSITION cannot be NULL")
    @NotEmpty(message = "Result POSITION cannot be EMPTY")
    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}