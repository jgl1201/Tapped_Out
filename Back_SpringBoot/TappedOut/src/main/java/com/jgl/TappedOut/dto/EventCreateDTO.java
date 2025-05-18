package com.jgl.TappedOut.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for creating a new event in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDTO {
    @NotNull(message = "Event SPORTID cannot be NULL")
    private Long sportId;

    @NotNull(message = "Organizer USERID cannot be NULL")
    private Long organizerId;

    @NotNull(message = "Event NAME cannot be NULL")
    @NotEmpty(message = "Event NAME cannot be EMPTY")
    @NotBlank(message = "Event NAME cannot be BLANK")
    private String name;

    private String description;

    @NotNull(message = "Event STARTDATE cannot be NULL")
    @FutureOrPresent(message = "Event STARTDATE must be in the future or present")
    private LocalDateTime startDate;

    @NotNull(message = "Event ENDDATE cannot be NULL")
    @Future(message = "Event ENDDATE must be in the future or present")
    private LocalDateTime endDate;

    @NotNull(message = "Event COUNTRY cannot be NULL")
    @NotEmpty(message = "Event COUNTRY cannot be EMPTY")
    @NotBlank(message = "Event COUNTRY cannot be BLANK")
    private String country;

    @NotNull(message = "Event CITY cannot be NULL")
    @NotEmpty(message = "Event CITY cannot be EMPTY")
    @NotBlank(message = "Event CITY cannot be BLANK")
    private String city;

    private String address;

    private String logo;

    private BigDecimal registrationFee;
}
