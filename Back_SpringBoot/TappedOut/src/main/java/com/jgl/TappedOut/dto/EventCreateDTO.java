package com.jgl.TappedOut.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for creating a new event in the system
 * 
 * @author Jorge García López
 * @version 1.1
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDTO {
    @NotNull(message = "Can't create a Event without Sport")
    private Long sportId;

    @NotNull(message = "Can't create a Event without Organizer")
    private Long organizerId;

    @NotBlank(message = "Can't create a Event without Name")
    @Size(max = 255, message = "Event's name's too long")
    private String name;

    private String description;

    @NotNull(message = "Can't create a Event without Starting Date")
    @Future(message = "Event's start date must be future")
    private LocalDateTime startDate;

    @NotNull(message = "Can't create a Event without Ending Date")
    @Future(message = "Event's end date must be future")
    private LocalDateTime endDate;

    @NotBlank(message = "Can't create a Event without Country")
    @Size(max = 100, message = "Event's country's too long")
    private String country;

    @NotBlank(message = "Can't create a Event without City")
    @Size(max = 100, message = "Event's city's too long")
    private String city;

    @Size(max = 255, message = "Event's address's too long")
    private String address;

    @URL(message = "Event's logo must be an URL")
    private String logo;

    @PositiveOrZero(message = "Event's registration fee must be 0 or above")
    private BigDecimal registrationFee;
}