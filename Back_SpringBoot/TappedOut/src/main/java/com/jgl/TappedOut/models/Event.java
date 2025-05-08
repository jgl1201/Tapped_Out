package com.jgl.TappedOut.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a sport event in the system
 * Contains information about the competition including
 * dates, location, organizer, and registration deails.
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Event SPORTID cannot be NULL")
    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sportId;

    @NotNull(message = "Event ORGANIZERID cannot be NULL")
    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizerId;

    @NotNull(message = "Event NAME cannot be NULL")
    @NotEmpty(message = "Event NAME cannot be EMPTY")
    @NotBlank(message = "Event NAME cannot be EMPTY")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Event STARTDATE cannot be NULL")
    @NotEmpty(message = "Event STARTDATE cannot be EMPTY")
    @FutureOrPresent(message = "Event STARTDATE must be either PRESENT OR FUTURE")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "Event ENDDATE cannot be NULL")
    @NotEmpty(message = "Event ENDDATE cannot be EMPTY")
    @Future(message = "Event ENDDATE must be in the FUTURE")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('PLANNED', 'ONGOING', 'COMPLETED', 'CANCELLED') DEFAULT 'PLANNED'")
    private EventStatus status = EventStatus.PLANNED;

    @NotNull(message = "Event COUNTRY cannot be NULL")
    @NotEmpty(message = "Event COUNTRY cannot be EMPTY")
    @NotBlank(message = "Event COUNTRY cannot be EMPTY")
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @NotNull(message = "Event CITY cannot be NULL")
    @NotEmpty(message = "Event CITY cannot be EMPTY")
    @NotBlank(message = "Event CITY cannot be EMPTY")
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "logo", length = 255)
    private String logo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "registration_fee", precision = 10, scale = 2)
    private BigDecimal registrationFee;
}