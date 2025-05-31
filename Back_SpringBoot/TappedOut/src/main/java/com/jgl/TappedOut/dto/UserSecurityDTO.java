package com.jgl.TappedOut.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a DTO for updating a user auth credentials in the system
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurityDTO {
    @NotBlank(message = "Can't update an User if Email empty")
    @Email(message = "User's email format is not valid")
    @Size(max = 255, message = "Email's too long")
    private String email;

    @NotBlank(message = "Can't update an User if Password empty")
    @Size(min = 8, max = 100, message = "Password must have between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,100}$", message = "Password must contain at least one uppercase letter, one lowercase letter and one number")
    private String newPassword;
}