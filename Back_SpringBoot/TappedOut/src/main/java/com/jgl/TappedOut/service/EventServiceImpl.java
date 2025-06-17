package com.jgl.TappedOut.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgl.TappedOut.dto.CategoryResponseDTO;
import com.jgl.TappedOut.dto.EventCreateDTO;
import com.jgl.TappedOut.dto.EventResponseDTO;
import com.jgl.TappedOut.dto.EventUpdateDTO;
import com.jgl.TappedOut.mapper.CategoryMapper;
import com.jgl.TappedOut.mapper.EventMapper;
import com.jgl.TappedOut.models.Category;
import com.jgl.TappedOut.models.Event;
import com.jgl.TappedOut.models.EventCategory;
import com.jgl.TappedOut.models.EventStatus;
import com.jgl.TappedOut.models.Sport;
import com.jgl.TappedOut.models.User;
import com.jgl.TappedOut.repositories.EventCategoryRepository;
import com.jgl.TappedOut.repositories.EventRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class to handle logic related with {@link Event}
 * 
 * ? Interacts with the EventRepository and other services to perform
 * ? CRUD operations and other specific domain operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
@Slf4j
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private EventCategoryRepository eventCategoryRepo;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SportServiceImpl sportService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryServiceImpl categoryService;


    /**
     * Retrieves all events
     * 
     * @return List of EventResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAllEvents() {
        log.debug("Fetching all Event");

        return eventRepo.findAll().stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves events by sport ID
     * 
     * @param sportId Sport ID
     * @return List of EventResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getEventsBySportId(Long sportId) {
        log.debug("Fetching Event\n\tSport ID: {}", sportId);

        Sport sport = sportService.findSportByIdOrThrow(sportId);

        return eventRepo.findBySportId(sport)
            .orElseThrow(() -> new EntityNotFoundException("Event not found for Sport ID: " + sportId))
            .stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves events by organizer ID
     * 
     * @param organizerId Organizer ID
     * @return List of EventResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getEventsByOrganizerId(Long organizerId) {
        log.debug("Fetching Event\n\tOrganizer(User) ID: {}", organizerId);

        User organizer = userService.findUserByIdOrThrow(organizerId);

        return eventRepo.findByOrganizerId(organizer)
            .orElseThrow(() -> new EntityNotFoundException("Event not found for Organizer ID: " + organizerId))
            .stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves events by status
     * 
     * @param status Event status
     * @return List of EventResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getEventsByStatus(EventStatus status) {
        log.debug("Fetching Event\n\tStatus: {}", status);

        return eventRepo.findByStatus(status)
            .orElseThrow(() -> new EntityNotFoundException("Event not found for Status: " + status))
            .stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves events by location
     * 
     * @param country Country name
     * @param city City name
     * @return List of EventResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getEventsByLocation(String country, String city) {
        log.debug("Fetching Event\n\tLocation -\n\t\tCountry: {}, City: {}", country, city);

        return eventRepo.findByCountryAndCity(country.trim(), city.trim())
            .orElseThrow(() -> new EntityNotFoundException("Event not found for location " + country + ", " + city))
            .stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves upcoming events
     * 
     * @return List of EventResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getUpcomingEvents() {
        log.debug("Fetching upcoming Event");

        return eventRepo.findUpcomingEvents()
            .orElseThrow(() -> new EntityNotFoundException("Event not found"))
            .stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves past events
     * 
     * @return List of EventResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getPastEvents() {
        log.debug("Fetching past Event");

        return eventRepo.findPastEvents()
            .orElseThrow(() -> new EntityNotFoundException("Event not found"))
            .stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves events by filters
     * 
     * @param sportId Sport ID
     * @param country Country name
     * @param city City name
     * @param query Search term
     * @return List of EventResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> searchEvents(Long sportId, String country, String city, String query) {
        log.debug("Searching Event\n\tFilters -\n\t\tSport: {},\n\t\tCountry: {}, City: {},\n\t\tQuery: {}",
        sportId, country, city, query);

        Sport sport = sportId != null ? sportService.findSportByIdOrThrow(sportId) : null;

        return eventRepo.searchEvents(sport, country, city, query)
            .orElseThrow(() -> new EntityNotFoundException("Event not found"))
            .stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves an event by ID
     * 
     * @param id Event ID
     * @return EventResponseDTO
     * @throws EntityNotFoundException if event not found
     */
    @Override
    @Transactional(readOnly = true)
    public EventResponseDTO getEventById(Long id) {
        log.debug("Fetching Event with ID: {}", id);

        Event event = findEventByIdOrThrow(id);

        return eventMapper.toResponseDTO(event);
    }

    /**
     * * The following methods handle the many-to-many relationship between Events and Categories
     * * through the EventCategory join table.
     * 
     * ? These methods manage the association between events and categories,
     * ? allowing categories to be added to or removed from events.
     */

    /**
     * Retrieves categories associated with an event
     * 
     * @param eventId Event ID
     * @return List of CategoryResponseDTO
     * @throws EntityNotFoundException if event not found from EventService
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getEventCategories(Long eventId) {
        log.debug("Fetching Category for Event with ID: {}", eventId);

        findEventByIdOrThrow(eventId);

        return eventCategoryRepo.findCategoriesByEventId(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"))
            .stream()
            .map(categoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Adds a category to an event
     * 
     * @param eventId Event ID
     * @param categoryId Category ID
     * @throws EntityNotFoundException if event not found
     * @throws EntityNotFoundException if category not found from CategoryService
     * @throws DataIntegrityViolationException if category already associated with event
     * @throws RuntimeException if an error occurs while adding the category to the event
     */
    @Override
    @Transactional
    public void addCategoryToEvent(Long eventId, Long categoryId) {
        log.info("Adding Category with ID: {} to Event with ID: {}", categoryId, eventId);
        
        Event event = findEventByIdOrThrow(eventId);
        Category category = categoryService.findCategoryByIdOrThrow(categoryId);

        validateCategorySportMatch(event, category);
        
        if (isCategoryAlreadyAssociated(event, category))
            throw new DataIntegrityViolationException("Category already associated with event");

        try {
            EventCategory eventCategory = new EventCategory(event, category);
            eventCategoryRepo.save(eventCategory);
            log.info("Successfully added category ID: {} to event ID: {}", categoryId, eventId);
        } catch (Exception e) {
            log.error("Error adding category to event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add category to event", e);
        }
    }

    /**
     * Removes a category from an event
     * 
     * @param eventId Event ID
     * @param categoryId Category ID
     * @throws EntityNotFoundException if event not found
     * @throws EntityNotFoundException if category not found from CategoryService
     * @throws EntityNotFoundException if category not associated with event
     * @throws RuntimeException if an error occurs while removing the category from the event
     */
    @Override
    @Transactional
    public void removeCategoryFromEvent(Long eventId, Long categoryId) {
        log.info("Removing Category with ID: {} from Event with ID: {}", categoryId, eventId);
        
        Event event = findEventByIdOrThrow(eventId);
        Category category = categoryService.findCategoryByIdOrThrow(categoryId);
        EventCategory eventCategory = findEventCategoryOrThrow(event, category);

        if (!isCategoryAlreadyAssociated(event, category))
            throw new EntityNotFoundException("Category not associated with this event");

        try {
            eventCategoryRepo.delete(eventCategory);
            log.info("Successfully removed category ID: {} from event ID: {}", categoryId, eventId);
        } catch (Exception e) {
            log.error("Error removing category from event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to remove category from event", e);
        }
    }

    /**
     * Creates a new event
     * 
     * @param dto EventCreateDTO
     * @return EventResponseDTO
     * @throws RuntimeException if an error occurs while creating the event
     */
    @Override
    @Transactional
    public EventResponseDTO createEvent(EventCreateDTO dto) {
        log.info("Creating new Event with name: {} \n\tSport:{}\n\tOrganizer:{}",
        dto.getName().trim(), dto.getSportId(), dto.getOrganizerId());

        sportService.findSportByIdOrThrow(dto.getSportId());
        userService.findUserByIdOrThrow(dto.getOrganizerId());

        validateEventDates(dto.getStartDate(), dto.getEndDate());
        
        try {
            Event event = eventMapper.fromCreateDTO(dto);
            event = eventRepo.save(event);
            log.info("Successfully created new event with ID: {}", event.getId());
            return eventMapper.toResponseDTO(event);
        } catch (Exception e) {
            log.error("Error creating event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create event", e);
        }
    }

    /**
     * Updates an existing event
     * 
     * @param id Event ID
     * @param dto EventUpdateDTO
     * @return EventResponseDTO
     * @throws EntityNotFoundException if event not found
     * @throws RuntimeException if an error occurs while updating the event
     */
    @Override
    @Transactional
    public EventResponseDTO updateEvent(Long id, EventUpdateDTO dto) {
        log.info("Updating Event with ID: {}", id);
        
        Event event = findEventByIdOrThrow(id);

        validateEventDates(dto.getStartDate(), dto.getEndDate());

        try {
            eventMapper.updateFromDTO(dto, event);
            Event updatedEvent = eventRepo.save(event);
            log.info("Successfully updated event with ID: {}", id);
            return eventMapper.toResponseDTO(updatedEvent);
        } catch (Exception e) {
            log.error("Error updating event with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update event", e);
        }
    }

    /**
     * Deletes an event by ID
     * 
     * @param id Event ID to delete
     * @throws EntityNotFoundException if event not found
     * @throws RuntimeException if an error occurs while deleting the event
     */
    @Override
    @Transactional
    public void deleteEvent(Long id) {
        log.info("Deleting Event with ID: {}", id);
        
        Event event = findEventByIdOrThrow(id);

        try {
            eventCategoryRepo.deleteByEventId(event);
            eventRepo.deleteById(id);
            log.info("Successfully deleted Event with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting Event with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete Event", e);
        }
    }

    /**
     * Method to find an event by ID
     * 
     * @param id Event ID
     * @return Event
     * @throws EntityNotFoundException if event not found
     */
    public Event findEventByIdOrThrow(Long id) {
        return eventRepo.findById(id)
            .orElseThrow(() -> {
                log.error("Event not found (ID: {})", id);
                throw new EntityNotFoundException("Event not found with ID: " + id);
            });
    }

    /**
     * Method to find an category belonging to an event
     * 
     * @param event Event
     * @param category Category
     * @return EventCategory
     * @throws EntityNotFoundException if category not found from CategoryService
     */
    public EventCategory findEventCategoryOrThrow(Event event, Category category) {
        return eventCategoryRepo.findByEventIdAndCategoryId(event, category)
            .orElseThrow(() -> {
                log.error("Category not found (ID: {})", category.getId());
                throw new EntityNotFoundException("Category not found with ID: " + category.getId());
            });
    }

    /**
     * Method to validate Event dates
     * 
     * @param startDate Event start date
     * @param endDate Event end date
     * @throws IllegalArgumentException if start date is after or equal to end date
     * @throws IllegalArgumentException if start date is before current date
     * @throws IllegalArgumentException if end date is before current date
     */
    public void validateEventDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate) || endDate.isBefore(startDate))
            throw new IllegalArgumentException("Start date must be before end date");

        if (startDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Start date must be after current date");
        
        if (endDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("End date must be after current date");
    }

    /**
     * Method to validate a category belongs to a sport before adding it to an event
     * 
     * @param event Event
     * @param category Category
     * @throws IllegalArgumentException if category does not belong to sport of event
     */
    public void validateCategorySportMatch(Event event, Category category) {
        if (!event.getSportId().getId().equals(category.getSportId().getId()))
            throw new IllegalArgumentException("Category does not belong to sport of event");
    }

    /**
     * Method to check if a category is already associated with an event
     * 
     * @param event Event
     * @param category Category
     * @return boolean
     */
    public boolean isCategoryAlreadyAssociated(Event event, Category category) {
        return eventCategoryRepo.existsByEventIdAndCategoryId(event, category);
    }
}