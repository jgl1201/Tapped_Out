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

import com.jgl.TappedOut.service.SportLevelService;
import com.jgl.TappedOut.dto.SportLevelResponseDTO;
import com.jgl.TappedOut.dto.SportLevelCreateDTO;
import com.jgl.TappedOut.dto.SportLevelUpdateDTO;

/**
 * Controller to define endpoints for SportLevel
 * 
 * @author Jorge García Lopez
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/sport-level")
@Tag(name = "SportLevel", description = "API Endpoints for Sport Level management")
public class SportLevelController extends BaseController {
    @Autowired
    private SportLevelService sportLevelService;

    /**
     * GET /api/tappedout/sport-level
     * Retrieves all sport levels
     * 
     * @return List of SportLevelResponseDTO
     */
    @GetMapping({"", "/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all sport levels",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of sport levels",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<SportLevelResponseDTO>> getAllSportLevels() {
        List<SportLevelResponseDTO> sportLevels = sportLevelService.getAllSportLevels();
        return ResponseEntity.ok(sportLevels);
    }

    /**
     * GET /api/tappedout/sport-level/sport/{sportId}
     * Retrieves all sport levels by SPORT
     * 
     * @param sportId the Sport IDsport level id
     * @return List of SportLevelResponseDTO
     * @throws EntityNotFoundException if sport not found
     */
    @GetMapping({"/sport/{sportId}", "/sport/{sportId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all sport levels for a specific sport",
        parameters = {
            @Parameter(name = "sportId", description = "Sport's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of sport levels",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<SportLevelResponseDTO>> getSportLevelsBySport(@PathVariable("sportId") Long sportId) {
        List<SportLevelResponseDTO> sportLevels = sportLevelService.getSportLevelsBySport(sportId);
        return ResponseEntity.ok(sportLevels);
    }

    /**
     * GET /api/tappedout/sport-level/{id}
     * Retrieves a sport level by ID
     * 
     * @param id the SportLevel ID
     * @return SportLevelResponseDTO
     * @throws EntityNotFoundException if sport level not found
     */
    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a specific Sport Level by ID",
        parameters = {
            @Parameter(name = "id", description = "Sport Level's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found sport level",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport level not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            )
        }
    )
    public ResponseEntity<SportLevelResponseDTO> getSportLevelById(@PathVariable("id") Long id) {
        SportLevelResponseDTO sportLevel = sportLevelService.getSportLevelById(id);
        return ResponseEntity.ok(sportLevel);
    }

    /**
     * GET /api/tappedout/sport-level/sport/{sportId}/name/{name}
     * Retrieves a sport level by sport ID and level NAME
     * 
     * @param sportId Sport ID
     * @param name SportLevel NAME
     * @return SportLevelResponseDTO
     * @throws EntityNotFoundException if sport or sport level not found
     */
    @GetMapping({"/sport/{sportId}/name/{name}", "/sport/{sportId}/name/{name}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a sport level by sport ID and level NAME",
        parameters = {
            @Parameter(name = "sportId", description = "Sport's ID", example = "1", required = true),
            @Parameter(name = "name", description = "Sport Level's NAME", example = "BEGINNER", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found sport level",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport or sport level not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            )
        }
    )
    public ResponseEntity<SportLevelResponseDTO> getSportLevelBySportAndName(@PathVariable("sportId") Long sportId, @PathVariable("name") String name) {
        SportLevelResponseDTO sportLevel = sportLevelService.getSportLevelBySportAndName(sportId, name);
        return ResponseEntity.ok(sportLevel);
    }

    /**
     * POST /api/tappedout/sport-level
     * Creates a new sport level
     * 
     * @param dto SportLevelCreateDTO
     * @return SportLevelResponseDTO
     * @throws EntityNotFoundException if sport not found
     * @throws DataIntegrityViolationException if sport level name already exists for the sport
     * @throws RuntimeException if failed to create sport level
     */
    @PostMapping({"", "/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Creates a new Sport Level",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Sport level created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Sport level with this name already exists for the sport",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            )
        }
    )
    public ResponseEntity<SportLevelResponseDTO> createSportLevel(@Valid @RequestBody SportLevelCreateDTO dto) {
        SportLevelResponseDTO created = sportLevelService.createSportLevel(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/tappedout/sport-level/{id}
     * Updates an existing sport level
     * 
     * @param id the SportLevel ID
     * @param dto SportLevelUpdateDTO
     * @return SportLevelResponseDTO
     * @throws EntityNotFoundException if sport level or sport not found
     * @throws DataIntegrityViolationException if new name already exists for the sport
     * @throws RuntimeException if failed to update sport level
     */
    @PutMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Updates an existing Sport Level",
        parameters = {
            @Parameter(name = "id", description = "Sport Level's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Sport level updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport level or sport not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Sport level with this name already exists for the sport",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SportLevelResponseDTO.class))
            )
        }
    )
    public ResponseEntity<SportLevelResponseDTO> updateSportLevel(@PathVariable("id") Long id, @Valid @RequestBody SportLevelUpdateDTO dto) {
        SportLevelResponseDTO updated = sportLevelService.updateSportLevel(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/tappedout/sport-level/{id}
     * Deletes a specific sport level
     * 
     * @param id the SportLevel ID
     * @throws EntityNotFoundException if sport level not found
     * @throws RuntimeException if failed to delete sport level
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Deletes a specific Sport Level by ID",
        parameters = {
            @Parameter(name = "id", description = "Sport Level's ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Sport level deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport level not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> deleteSportLevel(@PathVariable("id") Long id) {
        sportLevelService.deleteSportLevel(id);
        return ResponseEntity.noContent().build();
    }
}