package com.jgl.TappedOut.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a competitior's results for a specific event in the system
 * Contains information about the final position and optional notes
 * 
 * @author Jorge García López
 * @version 1.1
 * @since 2025
 */
@Entity
@Table(name = "results", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"event_id", "category_id", "competitor_id"}),
    @UniqueConstraint(columnNames = {"event_id", "category_id", "position"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event eventId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category categoryId;

    @ManyToOne
    @JoinColumn(name = "competitor_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User competitorId;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}