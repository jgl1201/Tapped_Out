package com.jgl.TappedOut.service;

import java.util.List;

import com.jgl.TappedOut.dto.CategoryResponseDTO;
import com.jgl.TappedOut.dto.EventCreateDTO;
import com.jgl.TappedOut.dto.EventResponseDTO;
import com.jgl.TappedOut.dto.EventUpdateDTO;
import com.jgl.TappedOut.models.EventStatus;

/**
 * Interface to declare methods needed at {@link EventServiceImpl}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public interface EventService {
    List<EventResponseDTO> getAllEvents();
    List<EventResponseDTO> getEventsBySportId(Long sportId);
    List<EventResponseDTO> getEventsByOrganizerId(Long organizerId);
    List<EventResponseDTO> getEventsByStatus(EventStatus status);
    List<EventResponseDTO> getEventsByLocation(String country, String city);
    List<EventResponseDTO> getUpcomingEvents();
    List<EventResponseDTO> getPastEvents();
    List<EventResponseDTO> searchEvents(Long sportId, String country, String city, String query);
    EventResponseDTO getEventById(Long id);
    List<CategoryResponseDTO> getEventCategories(Long eventId);
    void addCategoryToEvent(Long eventId, Long categoryId);
    void removeCategoryFromEvent(Long eventId, Long categoryId);
    EventResponseDTO createEvent(EventCreateDTO dto);
    EventResponseDTO updateEvent(Long id, EventUpdateDTO dto);
    void deleteEvent(Long id);
    void sendReminder();
}