package com.jgl.TappedOut.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,100}$", message = "Password must contain at least one uppercase letter, one lowercase letter and one number")
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9\\-\\.]{4,20}$", message = "ID Document format is invalid") //* Almost global validation regex
    private String dni;

    private Integer phone;

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    private String userType;

    private Long genderId;
}
