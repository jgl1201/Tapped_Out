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

import com.jgl.TappedOut.service.GenderService;
import com.jgl.TappedOut.dto.GenderResponseDTO;
import com.jgl.TappedOut.dto.GenderCreateDTO;

/**
 * Controller to define endpoints for Gender
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/gender")
@Tag(name = "Gender", description = "API Endpoints for Gender management")
public class GenderRestController {
    @Autowired
    private GenderService genderService;

    /**
     * GET /api/tappedout/gender    
     * Retrieves all genders
     * 
     * @return List of GenderResponseDTO
     */
    @GetMapping({"", "/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all genders",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of genders",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<GenderResponseDTO>> getAllGenders() {
        List<GenderResponseDTO> genders = genderService.getAllGenders();
        return ResponseEntity.ok(genders);
    }

    /**
     * GET /api/tappedout/gender/{id}
     * Retrieves a gender by ID
     * 
     * @param id the Gender ID
     * @return GenderResponseDTO
     * @throws EntityNotFoundException if gender not found
     */
    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a specific Gender by ID",
        parameters = {
            @Parameter(name = "id", description = "Gender's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found gender",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Gender not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            )
        }
    )
    public ResponseEntity<GenderResponseDTO> getGenderById(@PathVariable("id") Long id) {
        GenderResponseDTO gender = genderService.getGenderById(id);
        return ResponseEntity.ok(gender);
    }

    /**
     * GET /api/tappedout/gender/name/{name}
     * Retrieves a gender by NAME
     * 
     * @param name Gender NAME
     * @return GenderResponseDTO
     * @throws EntityNotFoundException if gender not found
     */
    @GetMapping({"/name/{name}", "/name/{name}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a specific Gender by NAME",
        parameters = {
            @Parameter(name = "name", description = "Gender's NAME", example = "Male", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found gender",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Gender not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            )
        }
    )
    public ResponseEntity<GenderResponseDTO> getGenderByName(@PathVariable("name") String name) {
        GenderResponseDTO gender = genderService.getGenderByName(name);
        return ResponseEntity.ok(gender);
    }

    /**
     * POST /api/tappedout/gender
     * Creates a new gender
     * 
     * @param dto GenderCreateDTO
     * @return GenderResponseDTO
     * @throws DataIntegrityViolationException if name already exist
     * @throws RuntimeException if gender creation fails
     */
    @PostMapping({"", "/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Creates a new Gender",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Gender created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Gender already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            )
        }
    )
    public ResponseEntity<GenderResponseDTO> createGender(@Valid @RequestBody GenderCreateDTO dto) {
        GenderResponseDTO created = genderService.createGender(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/tappedout/gender/{id}
     * Updates a specific gender
     * 
     * @param id the Gender ID
     * @param dto GenderCreateDTO
     * @return GenderResponseDTO
     * @throws EntityNotFoundException if gender not found
     * @throws DataIntegrityViolationException if name already exist
     * @throws RuntimeException if gender update fails
     */
    @PutMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Updates a specific Gender by ID",
        parameters = {
            @Parameter(name = "id", description = "Gender's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Gender updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Gender not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Gender already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenderResponseDTO.class))
            )
        }
    )
    public ResponseEntity<GenderResponseDTO> updateGender(@PathVariable("id") Long id, @Valid @RequestBody GenderCreateDTO dto) {
        GenderResponseDTO updated = genderService.updateGender(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/tappedout/gender/{id}
     * Deletes a specific gender
     * 
     * @param id the Gender ID
     * @throws EntityNotFoundException if gender not found
     * @throws RuntimeException if gender deletion fails
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Deletes a specific Gender by ID",
        parameters = {
            @Parameter(name = "id", description = "Gender's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Gender deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Gender not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> deleteGender(@PathVariable("id") Long id) {
        genderService.deleteGender(id);
        return ResponseEntity.noContent().build();
    }
}