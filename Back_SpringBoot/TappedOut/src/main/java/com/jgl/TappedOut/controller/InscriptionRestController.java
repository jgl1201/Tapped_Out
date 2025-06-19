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

import com.jgl.TappedOut.dto.InscriptionCreateDTO;
import com.jgl.TappedOut.dto.InscriptionResponseDTO;
import com.jgl.TappedOut.dto.InscriptionUpdateDTO;
import com.jgl.TappedOut.models.PaymentStatus;
import com.jgl.TappedOut.service.*;

/**
 * Controller to define endpoints for Inscription
 * 
 * @author Jorge García Lopez
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/inscription")
@Tag(name = "Inscription", description = "Endpoints for Inscription")
public class InscriptionRestController {
    @Autowired
    private InscriptionService inscriptionService;

    @SuppressWarnings("unused")
    @Autowired
    private PermissionsService permissionsService;

    /**
     * GET /api/tappedout/inscription
     * Retrieves all inscriptions
     * 
     * @return List of InscriptionResponseDTO
     */
    @GetMapping({"", "/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Retrieves all inscriptions",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of inscriptions",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<InscriptionResponseDTO>> getAllInscriptions() {
        List<InscriptionResponseDTO> inscriptions = inscriptionService.getAllInscriptions();
        return ResponseEntity.ok(inscriptions);
    }

    /**
     * GET /api/tappedout/inscription/competitor/{competitorId}
     * Retrieves inscriptions by competitor ID
     * 
     * @param competitorId Competitor ID
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if competitor not found
     */
    @GetMapping({"/competitor/{competitorId}", "/competitor/{competitorId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPETITOR') and @permissionsService.canSeeCompetitorInscriptions(#competitorId)")
    @Operation(
        summary = "Retrieves inscriptions by competitor ID",
        parameters = {
            @Parameter(name = "competitorId", description = "Competitor ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of inscriptions for the competitor",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Competitor not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<InscriptionResponseDTO>> getInscriptionByCompetitor(@PathVariable("competitorId") Long competitorId) {
        List<InscriptionResponseDTO> inscriptions = inscriptionService.getInscriptionsByCompetitor(competitorId);
        return ResponseEntity.ok(inscriptions);
    }

    /**
     * GET /api/tappedout/inscription/event/{eventId}
     * Retrieves inscriptions by event ID
     * 
     * @param eventId Event ID
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if event not found
     */
    @GetMapping({"/event/{eventId}", "/event/{eventId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canSeeInscriptions(#eventId)")
    @Operation(
        summary = "Retrieves inscriptions by event ID",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of inscriptions for the event",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<InscriptionResponseDTO>> getInscriptionsByEvent(@PathVariable("eventId") Long eventId) {
        List<InscriptionResponseDTO> inscriptions = inscriptionService.getInscriptionsByEvent(eventId);
        return ResponseEntity.ok(inscriptions);
    }

    /**
     * GET /api/tappedout/inscription/event/{eventId}/category/{categoryId}
     * Retrieves inscriptions by event and category ID
     * 
     * @param eventId Event ID
     * @param categoryId Category ID
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if event or category not found
     */
    @GetMapping({"/event/{eventId}/category/{categoryId}", "/event/{eventId}/category/{categoryId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canSeeInscriptions(#eventId, #categoryId)")
    @Operation(
        summary = "Retrieves inscriptions by event and category ID",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true),
            @Parameter(name = "categoryId", description = "Category ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of inscriptions for the event and category",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event or category not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<InscriptionResponseDTO>> getInscriptionsByCategory(@PathVariable("eventId") Long eventId, @PathVariable("categoryId") Long categoryId) {
        List<InscriptionResponseDTO> inscriptions = inscriptionService.getInscriptionsByCategory(eventId, categoryId);
        return ResponseEntity.ok(inscriptions);
    }

    /**
     * GET /api/tappedout/inscription/status/{status}
     * Retrieves inscriptions by payment status
     * 
     * @param status Payment status
     * @return List of InscriptionResponseDTO
     */
    @GetMapping({"/status/{status}", "/status/{status}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Retrieves inscriptions by payment status",
        parameters = {
            @Parameter(name = "status", description = "Payment status", example = "PAID", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of inscriptions with the payment status",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<InscriptionResponseDTO>> getInscriptionByPaymentStatus(@PathVariable("status") PaymentStatus status) {
        List<InscriptionResponseDTO> inscriptions = inscriptionService.getInscriptionByPaymentStatus(status);
        return ResponseEntity.ok(inscriptions);
    }

    /**
     * GET /api/tappedout/inscription/event/{eventId}/paid
     * Retrieves paid inscriptions for an event
     * 
     * @param eventId Event ID
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if event not found
     */
    @GetMapping({"/event/{eventId}/paid", "/event/{eventId}/paid/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canSeeInscriptions(#eventId)")
    @Operation(
        summary = "Retrieves paid inscriptions for an event",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of paid inscriptions for the event",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<InscriptionResponseDTO>> getPaidInscriptionsByEvent(@PathVariable("eventId") Long eventId) {
        List<InscriptionResponseDTO> inscriptions = inscriptionService.getPaidInscriptionsByEvent(eventId);
        return ResponseEntity.ok(inscriptions);
    }

    /**
     * GET /api/tappedout/inscription/event/{eventId}/paid/count
     * Counts paid inscriptions for an event
     * 
     * @param eventId Event ID
     * @return Number of paid inscriptions
     * @throws EntityNotFoundException if event not found
     */
    @GetMapping({"/event/{eventId}/paid/count", "/event/{eventId}/paid/count/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canSeeInscriptions(#eventId)")
    @Operation(
        summary = "Counts paid inscriptions for an event",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Count of paid inscriptions for the event",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Long.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Long> countPaidInscriptionsByEvent(@PathVariable("eventId") Long eventId) {
        Long count = inscriptionService.countPaidInscriptionsByEvent(eventId);
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/tappedout/inscription/{id}
     * Retrieves an inscription by ID
     * 
     * @param id Inscription ID
     * @return InscriptionResponseDTO
     * @throws EntityNotFoundException if inscription not found
     */
    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPETITOR') and @permissionsService.canSeeCompetitorInscriptions(#competitorId)")
    @Operation(
        summary = "Retrieves an inscription by ID",
        parameters = {
            @Parameter(name = "id", description = "Inscription ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found inscription",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Inscription not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<InscriptionResponseDTO> getInscriptionById(@PathVariable("id") Long id) {
        InscriptionResponseDTO inscription = inscriptionService.getInscriptionById(id);
        return ResponseEntity.ok(inscription);
    }

    /**
     * GET /api/tappedout/inscription/competitor/{competitorId}/event/{eventId}
     * Retrieves inscriptions by competitor and event
     * 
     * @param competitorId Competitor ID
     * @param eventId Event ID
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if competitor or event not found
     */
    @GetMapping({"/competitor/{competitorId}/event/{eventId}", "/competitor/{competitorId}/event/{eventId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR') and (@permissionsService.canSeeInscriptions(#eventId) or @permissionsService.canSeeCompetitorInscriptions(#competitorId))")
    @Operation(
        summary = "Retrieves inscriptions by competitor and event",
        parameters = {
            @Parameter(name = "competitorId", description = "Competitor ID", example = "1", required = true),
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found inscriptions",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Competitor or event not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<InscriptionResponseDTO>> getInscriptionsByCompetitorAndEvent(@PathVariable("competitorId") Long competitorId, @PathVariable("eventId") Long eventId) {
        List<InscriptionResponseDTO> inscriptions = inscriptionService.getInscriptionByCompetitorAndEvent(competitorId, eventId);
        return ResponseEntity.ok(inscriptions);
    }

    /**
     * POST /api/tappedout/inscription
     * Creates a new inscription
     * 
     * @param dto InscriptionCreateDTO
     * @return InscriptionResponseDTO
     * @throws EntityNotFoundException if competitor or event not found
     * @throws IllegalArgumentException if user already inscribed or incompatible with category
     */
    @PostMapping({"", "/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPETITOR')")
    @Operation(
        summary = "Creates a new inscription",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Inscription created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data or user already inscribed",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Competitor or event not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<InscriptionResponseDTO> createInscription(@Valid @RequestBody InscriptionCreateDTO dto) {
        InscriptionResponseDTO created = inscriptionService.createInscription(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/tappedout/inscription/{id}
     * Updates an inscription
     * 
     * @param id Inscription ID
     * @param dto InscriptionUpdateDTO
     * @return InscriptionResponseDTO
     * @throws EntityNotFoundException if inscription or category not found
     * @throws IllegalArgumentException if competitor incompatible with category
     */
    @PutMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPETITOR') and @permissionsService.canUpdateInscription(#id)")
    @Operation(
        summary = "Updates an inscription",
        parameters = {
            @Parameter(name = "id", description = "Inscription ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Inscription updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data or competitor incompatible with category",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Inscription or category not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InscriptionResponseDTO.class))
            )
        }
    )
    public ResponseEntity<InscriptionResponseDTO> updateInscription(@PathVariable("id") Long id, @Valid @RequestBody InscriptionUpdateDTO dto) {
        InscriptionResponseDTO updated = inscriptionService.updateInscription(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/tappedout/inscription/{id}
     * Deletes an inscription
     * 
     * @param id Inscription ID
     * @throws EntityNotFoundException if inscription not found
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPETITOR') and @permissionsService.canDeleteInscription(#id)")
    @Operation(
        summary = "Deletes an inscription",
        parameters = {
            @Parameter(name = "id", description = "Inscription ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Inscription deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Inscription not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> deleteInscription(@PathVariable("id") Long id) {
        inscriptionService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }
}