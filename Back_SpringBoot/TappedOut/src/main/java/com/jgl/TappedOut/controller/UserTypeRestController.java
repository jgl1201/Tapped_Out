package com.jgl.TappedOut.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import com.jgl.TappedOut.service.UserTypeService;
import com.jgl.TappedOut.dto.UserTypeResponseDTO;
import com.jgl.TappedOut.dto.UserTypeCreateDTO;

/**
 * Controller to define endpoints for UserType
 * 
 * @author Jorge García Lopez
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/user-types")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "UserType", description = "API Endpoints for User Type management")
public class UserTypeRestController {
    @Autowired
    private UserTypeService userTypeService;

    /**
     * GET /api/tappedout/user-type    
     * Retrieves all user types
     * 
     * @return List of UserTypeResponseDTO
     */
    @GetMapping({"", "/"})
    @Operation(
        summary = "Retrieves all user types",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of user types",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<UserTypeResponseDTO>> getAllUserTypes() {
        List<UserTypeResponseDTO> userTypes = userTypeService.getAllUserTypes();
        return ResponseEntity.ok(userTypes);
    }

    /**
     * GET /api/tappedout/user-type/{id}
     * Retrieves a user type by ID
     * 
     * @param id the UserType ID
     * @return UserTypeResponseDTO
     * @throws EntityNotFoundException if user type not found
     */
    @GetMapping({"/{id}", "/{id}/"})
    @Operation(
        summary = "Retrieves a specific User Type by ID",
        parameters = {
            @Parameter(name = "id", description = "User Type's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found user type",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User type not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserTypeResponseDTO> getUserTypeById(@PathVariable("id") Long id) {
        UserTypeResponseDTO userType = userTypeService.getUserTypeById(id);
        return ResponseEntity.ok(userType);
    }

    /**
     * GET /api/tappedout/user-type/name/{name}
     * Retrieves a user type by NAME
     * 
     * @param name UserType NAME
     * @return UserTypeResponseDTO
     * @throws EntityNotFoundException if user type not found
     */
    @GetMapping({"/name/{name}", "/name/{name}/"})
    @Operation(
        summary = "Retrieves a specific User Type by NAME",
        parameters = {
            @Parameter(name = "name", description = "User Type's NAME", example = "ADMIN", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found user type",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User type not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserTypeResponseDTO> getUserTypeByName(@PathVariable("name") String name) {
        UserTypeResponseDTO userType = userTypeService.getUserTypeByName(name);
        return ResponseEntity.ok(userType);
    }

    /**
     * POST /api/tappedout/user-type
     * Creates a new user type
     * 
     * @param dto UserTypeCreateDTO
     * @return UserTypeResponseDTO
     * @throws DataIntegrityViolationException if name already exist
     * @throws RuntimeException if user type creation fails
     */
    @PostMapping({"", "/"})
    @Operation(
        summary = "Creates a new User Type",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "User type created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "User type already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserTypeResponseDTO> createUserType(@Valid @RequestBody UserTypeCreateDTO dto) {
        UserTypeResponseDTO created = userTypeService.createUserType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/tappedout/user-type/{id}
     * Updates a specific user type
     * 
     * @param id the UserType ID
     * @param dto UserTypeCreateDTO
     * @return UserTypeResponseDTO
     * @throws EntityNotFoundException if user type not found
     * @throws DataIntegrityViolationException if name already exist
     * @throws RuntimeException if user type update fails
     */
    @PutMapping({"/{id}", "/{id}/"})
    @Operation(
        summary = "Updates a specific User Type by ID",
        parameters = {
            @Parameter(name = "id", description = "User Type's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User type updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User type not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "User type already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserTypeResponseDTO.class))
            )
        }
    )
    public ResponseEntity<UserTypeResponseDTO> updateUserType(@PathVariable("id") Long id, @Valid @RequestBody UserTypeCreateDTO dto) {
        UserTypeResponseDTO updated = userTypeService.updateUserType(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/tappedout/user-type/{id}
     * Deletes a specific user type
     * 
     * @param id the UserType ID
     * @throws EntityNotFoundException if user type not found
     * @throws RuntimeException if user type deletion fails
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @Operation(
        summary = "Deletes a specific User Type by ID",
        parameters = {
            @Parameter(name = "id", description = "User Type's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "User type deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User type not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> deleteUserType(@PathVariable("id") Long id) {
        userTypeService.deleteUserType(id);
        return ResponseEntity.noContent().build();
    }
}