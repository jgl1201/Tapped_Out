package com.jgl.TappedOut.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

import com.jgl.TappedOut.service.PermissionsService;
import com.jgl.TappedOut.service.UserService;
import com.jgl.TappedOut.dto.UserResponseDTO;
import com.jgl.TappedOut.dto.UserCreateDTO;
import com.jgl.TappedOut.dto.UserUpdateDTO;
import com.jgl.TappedOut.dto.UserSecurityDTO;

/**
 * Controller for defining endpoints for User
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "API Endpoints for User management")
public class UserRestController {
    @Autowired
    private UserService userService;

    @SuppressWarnings("unused")
    @Autowired
    private PermissionsService permissionsService;

    /**
     * GET /user    
     * Retrieves all users
     * 
     * @return List of UserResponseDTO
     */
    @GetMapping({"", "/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all users",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of users",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * GET /user/type/{typeId}
     * Retrieves all users by TYPE
     * 
     * @param typeId UserType ID
     * @return List of UserResponseDTO
     * @throws EntityNotFoundException if user type not found
     */
    @GetMapping({"/type/{typeId}", "/type/{typeId}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Retrieves all users for a specific user type",
        parameters = {
            @Parameter(name = "typeId", description = "User Type's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of users",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User type not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<UserResponseDTO>> getUsersByType(@PathVariable("typeId") Long typeId) {
        List<UserResponseDTO> users = userService.getUsersByType(typeId);
        return ResponseEntity.ok(users);
    }

    /**
     * GET /user/gender/{genderId}
     * Retrieves all users by GENDER
     * 
     * @param genderId Gender ID
     * @return List of UserResponseDTO
     * @throws EntityNotFoundException if gender not found
     */
    @GetMapping({"/gender/{genderId}", "/gender/{genderId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all users for a specific gender",
        parameters = {
            @Parameter(name = "genderId", description = "Gender's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of users",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Gender not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<UserResponseDTO>> getUsersByGender(@PathVariable("genderId") Long genderId) {
        List<UserResponseDTO> users = userService.getUsersByGender(genderId);
        return ResponseEntity.ok(users);
    }

    /**
     * GET /user/location
     * Retrieves all users by LOCATION
     * 
     * @param country Country
     * @param city City
     * @return List of UserResponseDTO
     * @throws EntityNotFoundException if location not found
     */
    @GetMapping({"/location", "/location/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Retrieves all users for a specific location",
        parameters = {
            @Parameter(name = "country", description = "Country", example = "Spain", required = true),
            @Parameter(name = "city", description = "City", example = "Madrid", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of users",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<UserResponseDTO>> getUsersByLocation(@RequestParam("country") String country, @RequestParam("city") String city) {
        List<UserResponseDTO> users = userService.getUsersByLocation(country, city);
        return ResponseEntity.ok(users);
    }

    /**
     * GET /user/search
     * Retrieves all users by NAME or EMAIL
     * 
     * @param query Search term
     * @return List of UserResponseDTO
     */
    @GetMapping({"/search", "/search/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all users by name or email",
        parameters = {
            @Parameter(name = "query", description = "Search term", example = "John", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found matching of users",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam("query") String query) {
        List<UserResponseDTO> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    /**
     * GET /user/{id}
     * Retrieves a user by ID
     * 
     * @param userId User ID
     * @return UserResponseDTO
     * @throws EntityNotFoundException if user not found
     */
    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a user by ID",
        parameters = {
            @Parameter(name = "id", description = "User's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found user",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /user/dni/{dni}
     * Retrieves a user by DNI
     * 
     * @param dni User DNI
     * @return UserResponseDTO
     * @throws EntityNotFoundException if user not found
     */
    @GetMapping({"/dni/{dni}", "/dni/{dni}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Retrieves a user by DNI",
        parameters = {
            @Parameter(name = "dni", description = "User's DNI", example = "12345678A", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found user",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserResponseDTO> getUserByDni(@PathVariable("dni") String dni) {
        UserResponseDTO user = userService.getUserByDni(dni);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /user/email/{email}
     * Retrieves a user by EMAIL
     * 
     * @param email User EMAIL
     * @return UserResponseDTO
     * @throws EntityNotFoundException if user not found
     */
    @GetMapping({"/email/{email}", "/email/{email}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Retrieves a user by EMAIL",
        parameters = {
            @Parameter(name = "email", description = "User's EMAIL", example = "Vt8j0@example.com", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found user",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable("email") String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * POST /user
     * Creates a new user
     * 
     * @param dto UserCreateDTO
     * @return UserResponseDTO
     * @throws DataIntegrityViolationException if DNI or EMAIL already exist
     * @throws RuntimeEsception if user creation fails
     */
    @PostMapping({"", "/"})
    @Operation(
        summary = "Creates a new User",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "User created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "User already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        UserResponseDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /user/{id}
     * Updates a user
     * 
     * @param id User ID
     * @param dto UserUpdateDTO
     * @return UserResponseDTO
     * @throws EntityNotFoundException if user not found
     * @throws DataIntegrityViolationException if DNI or EMAIL change and already exist
     * @throws RuntimeException if user update fails
     */
    @PutMapping({"/{id}", "/{id}/"})
    @PreAuthorize("@permissionsService.canEditUser(#id)")
    @Operation(
        summary = "Updates a user basic information",
        parameters = {
            @Parameter(name = "id", description = "User's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "User (changed unique field(s)) already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDTO dto) {
        UserResponseDTO updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * PATCH /user/{id}/security
     * Updates a user auth information (email and password)
     * 
     * @param id User ID
     * @param dto UserSecurityDTO
     * @return UserResponseDTO
     * @throws EntityNotFoundException if user not found
     * @throws RuntimeException if user update fails
     */
    @PatchMapping({"/{id}/security", "/{id}/security/"})
    @PreAuthorize("@permissionsService.canEditUser(#id)")
    @Operation(
        summary = "Updates user authentication information",
        parameters = {
            @Parameter(name = "id", description = "User's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "User (changed unique field(s)) already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserResponseDTO> updateUserSecurity(@PathVariable("id") Long id, @Valid @RequestBody UserSecurityDTO dto) {
        UserResponseDTO updated = userService.updateUserSecurity(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /user/{id}
     * Deletes a user
     * 
     * @param id User ID
     * @throws EntityNotFoundException if user not found
     * @throws RuntimeException if user deletion fails
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Deletes a user by ID",
        parameters = {
            @Parameter(name = "id", description = "User's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "User deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}