package com.jgl.TappedOut.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.URL;

import com.jgl.TappedOut.models.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for creating a new user in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    @NotBlank(message = "Can't create a User without DNI")
    @Pattern(regexp = "^[a-zA-Z0-9\\-\\.]{4,20}$", message = "ID Document format is invalid") //* Almost global validation regex
    private String dni;

    @NotNull(message = "Can't create a User without Type")
    private Long typeId;

    @NotBlank(message = "Can't create a User without Email")
    @Email(message = "Email's format isn't valid")
    @Size(max = 255, message = "Email's too long")
    private String email;

    @NotBlank(message = "Can't create a User without Password")
    @Size(min = 8, max = 100, message = "Password must have between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,100}$", message = "Password must contain at least one uppercase letter, one lowercase letter and one number")
    private String password;

    @NotBlank(message = "Can't create a User without First Name")
    @Size(max = 100, message = "User's first name's too long")
    private String firstName;

    @NotBlank(message = "Can't create a User without Last Name")
    @Size(max = 100, message = "User's last name's too long")
    private String lastName;

    @NotNull(message = "Can't create a User without Date Of Birth")
    @Past(message = "User's date of birth must be past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Can't create a User without Gender")
    private Gender genderId;

    @NotBlank(message = "Can't create a User without Country")
    private String country;

    @NotBlank(message = "Can't create a User without City")
    private String city;

    private Integer phone;

    @URL(message = "User's avatar must be an URL")
    private String avatar;
}