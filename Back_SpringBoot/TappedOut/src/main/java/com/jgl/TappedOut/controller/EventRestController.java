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

import com.jgl.TappedOut.service.EventService;
import com.jgl.TappedOut.service.PermissionsService;
import com.jgl.TappedOut.dto.EventResponseDTO;
import com.jgl.TappedOut.dto.EventCreateDTO;
import com.jgl.TappedOut.dto.EventUpdateDTO;
import com.jgl.TappedOut.models.EventStatus;
import com.jgl.TappedOut.dto.CategoryResponseDTO;

/**
 * Controller for defining endpoints for Event
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/event")
@Tag(name = "Event", description = "API Endpoints for Event")
public class EventRestController {
    @Autowired
    private EventService eventService;

    @SuppressWarnings("unused")
    @Autowired
    private PermissionsService permissionsService;

    /**
     * GET /api/tappedout/event
     * Retrieves all events
     * 
     * @return List of EventResponseDTO
     */
    @GetMapping({"", "/"})
    @Operation(
        summary = "Retrieves all events",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of events",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/tappedout/event/sport/{sportId}
     * Retrieves events by sport ID
     * 
     * @param sportId Sport ID
     * @return List of EventResponseDTO
     */
    @GetMapping({"/sport/{sportId}", "/sport/{sportId}/"})
    @Operation(
        summary = "Retrieves all events for a specific sport",
        parameters = {
            @Parameter(name = "sportId", description = "Sport ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of events for the sport",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<EventResponseDTO>> getEventsBySport(@PathVariable("sportId") Long sportId) {
        List<EventResponseDTO> events = eventService.getEventsBySportId(sportId);
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/tappedout/event/organizer/{organizerId}
     * Retrieves events by organizer ID
     * 
     * @param organizerId Organizer ID
     * @return List of EventResponseDTO
     */
    @GetMapping({"/organizer/{organizerId}", "/organizer/{organizerId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all events of an organizer",
        parameters = {
            @Parameter(name = "organizerId", description = "Organizer (User) ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of events for the organizer",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Organizer not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<EventResponseDTO>> getEventsByOrganizer(@PathVariable("organizerId") Long organizerId) {
        List<EventResponseDTO> events = eventService.getEventsByOrganizerId(organizerId);
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/tappedout/event/status/{status}
     * Retrieves events by status
     * 
     * @param status Event status
     * @return List of EventResponseDTO
     */
    @GetMapping({"/status/{status}", "/status/{status}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all events by status",
        parameters = {
            @Parameter(name = "status", description = "Event status", example = "PLANNED", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of events with the status",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<EventResponseDTO>> getEventsByStatus(@PathVariable("status") EventStatus status) {
        List<EventResponseDTO> events = eventService.getEventsByStatus(status);
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/tappedout/event/location
     * Retrieves events by location
     * 
     * @param country Country name
     * @param city City name
     * @return List of EventResponseDTO
     */
    @GetMapping({"/location", "/location/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves events by location",
        parameters = {
            @Parameter(name = "country", description = "Country name", example = "Spain", required = true),
            @Parameter(name = "city", description = "City name", example = "Madrid", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of events for the location",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<EventResponseDTO>> getEventsByLocation(@RequestParam("country") String country, @RequestParam("city") String city) {
        List<EventResponseDTO> events = eventService.getEventsByLocation(country, city);
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/tappedout/event/upcoming
     * Retrieves upcoming events
     * 
     * @return List of EventResponseDTO
     */
    @GetMapping({"/upcoming", "/upcoming/"})
    @Operation(
        summary = "Retrieves upcoming events",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of upcoming events",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<EventResponseDTO>> getUpcomingEvents() {
        List<EventResponseDTO> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/tappedout/event/past
     * Retrieves past events
     * 
     * @return List of EventResponseDTO
     */
    @GetMapping({"/past", "/past/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves past events",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of past events",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<EventResponseDTO>> getPastEvents() {
        List<EventResponseDTO> events = eventService.getPastEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/tappedout/event/search
     * Searches events by filters
     * 
     * @param sportId Sport ID (optional)
     * @param country Country name (optional)
     * @param city City name (optional)
     * @param query Search term (optional)
     * @return List of EventResponseDTO
     */
    @GetMapping({"/search", "/search/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Searches events by filters",
        parameters = {
            @Parameter(name = "sportId", description = "Sport ID", example = "1", required = false),
            @Parameter(name = "country", description = "Country name", example = "Spain", required = false),
            @Parameter(name = "city", description = "City name", example = "Madrid", required = false),
            @Parameter(name = "query", description = "Search term", example = "tournament", required = false)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found matching events",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<EventResponseDTO>> searchEvents(@RequestParam(value = "sportId", required = false) Long sportId, @RequestParam(value = "country", required = false) String country, @RequestParam(value = "city", required = false) String city, @RequestParam(value = "query", required = false) String query) {
        List<EventResponseDTO> events = eventService.searchEvents(sportId, country, city, query);
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/tappedout/event/{id}
     * Retrieves an event by ID
     * 
     * @param id Event ID
     * @return EventResponseDTO
     * @throws EntityNotFoundException if event not found
     */
    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves an event by ID",
        parameters = {
            @Parameter(name = "id", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found event",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable("id") Long id) {
        EventResponseDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    /**
     * GET /api/tappedout/event/{eventId}/categories
     * Retrieves categories associated with an event
     * 
     * @param eventId Event ID
     * @return List of CategoryResponseDTO
     * @throws EntityNotFoundException if event not found
     */
    @GetMapping({"/{eventId}/categories", "/{eventId}/categories/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves categories associated with an event",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found categories for the event",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<CategoryResponseDTO>> getEventCategories(@PathVariable("eventId") Long eventId) {
        List<CategoryResponseDTO> categories = eventService.getEventCategories(eventId);
        return ResponseEntity.ok(categories);
    }

    /**
     * POST /api/tappedout/event/{eventId}/category/{categoryId}
     * Adds a category to an event
     * 
     * @param eventId Event ID
     * @param categoryId Category ID
     * @throws EntityNotFoundException if event or category not found
     * @throws DataIntegrityViolationException if category already associated with event
     * @throws RuntimeException if failed to add category to event
     */
    @PostMapping({"/{eventId}/category/{categoryId}", "/{eventId}/category/{categoryId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canEditEvent(#eventId)")
    @Operation(
        summary = "Adds a category to an event",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true),
            @Parameter(name = "categoryId", description = "Category ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Category added to event successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Category does not belong to sport of event"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event or category not found"
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Category already associated with event"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> addCategoryToEvent(@PathVariable("eventId") Long eventId, @PathVariable("categoryId") Long categoryId) {
        eventService.addCategoryToEvent(eventId, categoryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/tappedout/event/{eventId}/category/{categoryId}
     * Removes a category from an event
     * 
     * @param eventId Event ID
     * @param categoryId Category ID
     * @throws EntityNotFoundException if event, category not found or category not associated with event
     * @throws RuntimeException if failed to remove category from event
     */
    @DeleteMapping({"/{eventId}/category/{categoryId}", "/{eventId}/category/{categoryId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canEditEvent(#eventId)")
    @Operation(
        summary = "Removes a category from an event",
        parameters = {
            @Parameter(name = "eventId", description = "Event ID", example = "1", required = true),
            @Parameter(name = "categoryId", description = "Category ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Category removed from event successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event, category not found or category not associated with event"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> removeCategoryFromEvent(@PathVariable("eventId") Long eventId, @PathVariable("categoryId") Long categoryId) {
        eventService.removeCategoryFromEvent(eventId, categoryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/tappedout/event
     * Creates a new event
     * 
     * @param dto EventCreateDTO
     * @return EventResponseDTO
     * @throws RuntimeException if failed to create event
     */
    @PostMapping({"", "/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    @Operation(
        summary = "Creates a new event",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Event created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid event dates",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport or category not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventCreateDTO dto) {
        EventResponseDTO created = eventService.createEvent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/tappedout/event/{id}
     * Updates an existing event
     * 
     * @param id Event ID
     * @param dto EventUpdateDTO
     * @return EventResponseDTO
     * @throws EntityNotFoundException if event not found
     * @throws RuntimeException if failed to update event
     */
    @PutMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canEditEvent(#id)")
    @Operation(
        summary = "Updates an existing event",
        parameters = {
            @Parameter(name = "id", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Event updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid event dates",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Event, sport or category not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDTO.class))
            )
        }
    )
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable("id") Long id, @Valid @RequestBody EventUpdateDTO dto) {
        EventResponseDTO updated = eventService.updateEvent(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/tappedout/event/{id}
     * Deletes an event by ID
     * 
     * @param id Event ID
     * @throws EntityNotFoundException if event not found
     * @throws RuntimeException if failed to delete event
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER') and @permissionsService.canEditEvent(#id)")
    @Operation(
        summary = "Deletes an event by ID",
        parameters = {
            @Parameter(name = "id", description = "Event ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Event deleted successfully"
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
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}