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

import com.jgl.TappedOut.service.SportService;
import com.jgl.TappedOut.dto.SportResponseDTO;
import com.jgl.TappedOut.dto.SportCreateDTO;

/**
 * Controller to define endpoints for Sport
 * 
 * @author Jorge García Lopez
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/sport")
@Tag(name = "Sports", description = "API Endpoints for Sport management")
public class SportRestController {
    @Autowired
    private SportService sportService;

    /**
     * GET /api/tappedout/sport
     * Retrieves all sports
     * 
     * @return List of SportResponseDTO
     */
    @GetMapping({"", "/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all sports",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of sports",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<SportResponseDTO>> getAllSports() {
        List<SportResponseDTO> sports = sportService.getAllSports();
        return ResponseEntity.ok(sports);
    }

    /**
     * GET /api/tappedout/sport/{id}
     * Retrieves a sport by ID
     * 
     * @param id the Sport ID
     * @return SportResponseDTO
     * @throws EntityNotFoundException if sport not found
     */
    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a specific Sport by ID",
        parameters = {
            @Parameter(name = "id", description = "Sport's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found sport",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            )
        }
    )
    public ResponseEntity<SportResponseDTO> getSportById(@PathVariable("id") Long id) {
        SportResponseDTO sport = sportService.getSportById(id);
        return ResponseEntity.ok(sport);
    }

    /**
     * GET /api/tappedout/sport/name/{name}
     * Retrieves a sport by NAME
     * 
     * @param name Sport NAME
     * @return SportResponseDTO
     * @throws EntityNotFoundException if sport not found
     */
    @GetMapping({"/name/{name}", "/name/{name}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a specific Sport by NAME",
        parameters = {
            @Parameter(name = "name", description = "Sport's NAME", example = "Brazilian Jiu-Jitsu", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found sport",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            )
        }
    )
    public ResponseEntity<SportResponseDTO> getSportByName(@PathVariable("name") String name) {
        SportResponseDTO sport = sportService.getSportByName(name);
        return ResponseEntity.ok(sport);
    }

    /**
     * POST /api/tappedout/sport
     * Creates a new sport
     * 
     * @param dto SportCreateDTO
     * @return SportResponseDTO
     * @throws DataIntegrityViolationException if name already exist
     * @throws RuntimeException if sport creation fails
     */
    @PostMapping({"", "/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Creates a new Sport",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Sport created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Sport already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            )
        }
    )
    public ResponseEntity<SportResponseDTO> createSport(@Valid @RequestBody SportCreateDTO dto) {
        SportResponseDTO created = sportService.createSport(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/tappedout/sport/{id}
     * Updates a specific sport
     * 
     * @param id the Sport ID
     * @param dto SportCreateDTO
     * @return SportResponseDTO
     * @throws EntityNotFoundException if sport not found
     * @throws DataIntegrityViolationException if name already exist
     * @throws RuntimeException if sport update fails
     */
    @PutMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Updates a specific Sport by ID",
        parameters = {
            @Parameter(name = "id", description = "Sport's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Sport updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Sport already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportResponseDTO.class))
            )
        }
    )
    public ResponseEntity<SportResponseDTO> updateSport(@PathVariable("id") Long id, @Valid @RequestBody SportCreateDTO dto) {
        SportResponseDTO updated = sportService.updateSport(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/tappedout/sport/{id}
     * Deletes a specific sport
     * 
     * @param id the Sport ID
     * @throws EntityNotFoundException if sport not found
     * @throws RuntimeException if sport deletion fails
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Deletes a specific Sport by ID",
        parameters = {
            @Parameter(name = "id", description = "Sport's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Sport deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> deleteSport(@PathVariable("id") Long id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }
}