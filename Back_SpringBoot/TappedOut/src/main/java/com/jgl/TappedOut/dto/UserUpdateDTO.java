package com.jgl.TappedOut.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for updating a user in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @NotBlank(message = "Can't update a User without First Name")
    @Size(max = 255, message = "User's first name's too long")
    private String firstName;

    @NotBlank(message = "Can't update a User without Last Name")
    @Size(max = 255, message = "User's last name's too long")
    private String lastName;

    @NotNull(message = "Can't update a User without Date Of Birth")
    @Past(message = "User's date o birth must be past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Can't update a User without Country")
    private String country;

    @NotBlank(message = "Can't update a User without City")
    private String city;

    private Integer phone;

    private Boolean isVerified;
}