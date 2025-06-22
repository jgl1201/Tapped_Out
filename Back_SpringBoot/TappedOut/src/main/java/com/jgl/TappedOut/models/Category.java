package com.jgl.TappedOut.models;

import java.math.BigDecimal;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a competition category in the system.
 * Categories define the specific divisions for events based on
 * sport, gender, age range, weight range and skill level (optional)
 * 
 * @author Jorge García López
 * @version 1.1
 * @since 2025
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sport sportId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "min_age")
    private Integer minAge = 0;

    @Column(name = "max_age")
    private Integer maxAge = 100;

    @Column(name = "min_weight", precision = 5, scale = 2)
    private BigDecimal minWeight;

    @Column(name = "max_weight", precision = 5, scale = 2)
    private BigDecimal maxWeight;

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Gender genderId;

    @ManyToOne
    @JoinColumn(name = "level_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private SportLevel levelId;
}