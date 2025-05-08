package com.jgl.TappedOut.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user in the system
 * Contains all personal information, authentication details and account status
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "users", indexes = @Index(name = "idx_dni", columnList = "dni") )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User DNI cannot be NULL")
    @NotEmpty(message = "User DNI cannot be EMPTY")
    @NotBlank(message = "User DNI cannot be BLANK")
    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String dni;

    @NotNull(message = "User TYPE cannot be NULL")
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private UserType typeId;

    @NotNull(message = "User EMAIL cannot be NULL")
    @NotEmpty(message = "User EMAIL cannot be EMPTY")
    @NotBlank(message = "User EMAIL cannot be BLANK")
    @Email(message = "User EMAIL must have a VALID FORMAT")
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotNull(message = "User PASSWORD_HASH cannot be NULL")
    @NotEmpty(message = "User PASSWORD_HASH cannot be EMPTY")
    @NotBlank(message = "User PASSWORD_HASH cannot be BLANK")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotNull(message = "User FIRST NAME cannot be NULL")
    @NotEmpty(message = "User FIRST NAME cannot be EMPTY")
    @NotBlank(message = "User FIRST NAME cannot be BLANK")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotNull(message = "User LAST NAME cannot be NULL")
    @NotEmpty(message = "User LAST NAME cannot be EMPTY")
    @NotBlank(message = "User LAST NAME cannot be BLANK")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotNull(message = "User DATE OF BIRTH cannot be NULL")
    @NotEmpty(message = "User DATE OF BIRTH cannot be EMPTY")
    @Past(message = "User DATE OF BIRTH must be from the PAST")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull(message = "User GENDER cannot be NULL")
    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender genderId;

    @NotNull(message = "User COUNTRY cannot be NULL")
    @NotEmpty(message = "User COUNTRY cannot be EMPTY")
    @NotBlank(message = "User COUNTRY cannot be BLANK")
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @NotNull(message = "User CITY cannot be NULL")
    @NotEmpty(message = "User CITY cannot be EMPTY")
    @NotBlank(message = "User CITY cannot be BLANK")
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "phone", length = 20)
    private Integer phone;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "is_verified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isVerified;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}