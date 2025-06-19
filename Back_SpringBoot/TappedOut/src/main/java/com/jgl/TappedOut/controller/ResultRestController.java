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
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

import com.jgl.TappedOut.service.PermissionsService;
import com.jgl.TappedOut.service.ResultService;
import com.jgl.TappedOut.dto.ResultResponseDTO;
import com.jgl.TappedOut.dto.ResultCreateDTO;
import com.jgl.TappedOut.dto.ResultUpdateDTO;

/**
 * Controller to define endpoints for Result
 * 
 * @author Jorge García Lopez
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/results")
@Tag(name = "Result", description = "Endpoints for Result")
public class ResultRestController {
    @Autowired
    private ResultService resultService;

    @SuppressWarnings("unused")
    @Autowired
    private PermissionsService permissionsService;

    /**
     * GET /result    
     * Retrieves all results
     * 
     * @return List of ResultResponseDTO
     */
    @GetMapping({"", "/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all results",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of results",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<ResultResponseDTO>> getAllResults() {
        List<ResultResponseDTO> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }

    /**
     * GET /result/event/{eventId}
     * Retrieves results by event ID
     * 
     * @param eventId Event ID
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if event not found
     */
    @GetMapping({"/event/{eventId}", "/event/{eventId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves results by event ID",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of results for the event",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<ResultResponseDTO>> getResultsByEvent(@PathVariable("eventId") Long eventId) {
        List<ResultResponseDTO> results = resultService.getResultsByEvent(eventId);
        return ResponseEntity.ok(results);
    }

    /**
     * GET /result/event/{eventId}/category/{categoryId}
     * Retrieves results by event and category ID
     * 
     * @param eventId Event ID
     * @param categoryId Category ID
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if event or category not found
     */
    @GetMapping({"/event/{eventId}/category/{categoryId}", "/event/{eventId}/category/{categoryId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves results by event and category ID",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true),
            @Parameter(name = "categoryId", description = "Category ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of results for the event and category",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event or category not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<ResultResponseDTO>> getResultsByEventAndCategory(@PathVariable("eventId") Long eventId, @PathVariable("categoryId") Long categoryId) {
        List<ResultResponseDTO> results = resultService.getResultsByEventAndCategory(eventId, categoryId);
        return ResponseEntity.ok(results);
    }

    /**
     * GET /result/competitor/{competitorId}/event/{eventId}
     * Retrieves results by competitor and event ID
     * 
     * @param competitorId Competitor ID
     * @param eventId Event ID
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if competitor or event not found
     */
    @GetMapping({"/competitor/{competitorId}/event/{eventId}", "/competitor/{competitorId}/event/{eventId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves results by competitor and event ID",
        parameters = {
            @Parameter(name = "competitorId", description = "Competitor ID", example = "1", required = true),
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of results for the competitor and event",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Competitor or event not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<ResultResponseDTO>> getResultsByCompetitorAndEvent(@PathVariable("competitorId") Long competitorId, @PathVariable("eventId") Long eventId) {
        List<ResultResponseDTO> results = resultService.getResultsByEventAndCompetitor(eventId, competitorId);
        return ResponseEntity.ok(results);
    }

    /**
     * GET /result/competitor/{competitorId}
     * Retrieves results by competitor ID
     * 
     * @param competitorId Competitor ID
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if competitor not found
     */
    @GetMapping({"/competitor/{competitorId}", "/competitor/{competitorId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves results by competitor ID",
        parameters = {
            @Parameter(name = "competitorId", description = "Competitor ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of results for the competitor",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Competitor not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<ResultResponseDTO>> getResultsByCompetitor(@PathVariable("competitorId") Long competitorId) {
        List<ResultResponseDTO> results = resultService.getResultsByCompetitor(competitorId);
        return ResponseEntity.ok(results);
    }

    /**
     * GET /result/event/{eventId}/position/{position}
     * Retrieves results by event ID and position
     * 
     * @param eventId Event ID
     * @param position Position
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if event not found
     */
    @GetMapping({"/event/{eventId}/position/{position}", "/event/{eventId}/position/{position}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves results by event ID and position",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true),
            @Parameter(name = "position", description = "Position", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of results for the event and position",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<ResultResponseDTO>> getResultsByEventAndPosition(@PathVariable("eventId") Long eventId, @PathVariable("position") Integer position) {
        List<ResultResponseDTO> results = resultService.getResultsByEventAndPosition(eventId, position);
        return ResponseEntity.ok(results);
    }

    /**
     * GET /result/{id}
     * Retrieves a result by ID
     * 
     * @param id Result ID
     * @return ResultResponseDTO
     * @throws EntityNotFoundException if result not found
     */
    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a result by ID",
        parameters = {
            @Parameter(name = "id", description = "Result ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found result",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Result not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<ResultResponseDTO> getResultById(@PathVariable("id") Long id) {
        ResultResponseDTO result = resultService.getResultById(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /result/event/{eventId}/category/{categoryId}/winners
     * Retrieves winners by event and category ID
     * 
     * @param eventId Event ID
     * @param categoryId Category ID
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if event or category not found
     */
    @GetMapping({"/event/{eventId}/category/{categoryId}/winners", "/event/{eventId}/category/{categoryId}/winners/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves winners by event and category ID",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true),
            @Parameter(name = "categoryId", description = "Category ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found winners for the event and category",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event or category not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<ResultResponseDTO> getWinnerByEventAndCategory(@PathVariable("eventId") Long eventId, @PathVariable("categoryId") Long categoryId) {
        ResultResponseDTO winners = resultService.getWinnerByEventAndCategory(eventId, categoryId);
        return ResponseEntity.ok(winners);
    }

    /**
     * POST /result
     * Creates a new result
     * 
     * @param dto ResultCreateDTO
     * @return ResultResponseDTO
     * @throws EntityNotFoundException if event, category or competitor not found
     * @throws IllegalArgumentException if competitor not inscribed or position not unique
     */
    @PostMapping({"", "/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canEditResults(#dto.getEventId)")
    @Operation(
        summary = "Creates a new result",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Result created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data or competitor not inscribed or position not unique",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event, category or competitor not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<ResultResponseDTO> createResult(@Valid @RequestBody ResultCreateDTO dto) {
        ResultResponseDTO created = resultService.createResult(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /result/{id}
     * Updates a result
     * 
     * @param id Result ID
     * @param dto ResultUpdateDTO
     * @return ResultResponseDTO
     * @throws EntityNotFoundException if result not found
     * @throws IllegalArgumentException if position not unique
     */
    @PutMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canEditResult(#id)")
    @Operation(
        summary = "Updates a result",
        parameters = {
            @Parameter(name = "id", description = "Result ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Result updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data or position not unique",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Result not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResultResponseDTO.class))
            )
        }
    )
    public ResponseEntity<ResultResponseDTO> updateResult(@PathVariable("id") Long id, @Valid @RequestBody ResultUpdateDTO dto) {
        ResultResponseDTO updated = resultService.updateResult(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /result/{id}
     * Deletes a result
     * 
     * @param id Result ID
     * @throws EntityNotFoundException if result not found
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Deletes a result",
        parameters = {
            @Parameter(name = "id", description = "Result ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Result deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Result not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> deleteResult(@PathVariable("id") Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}