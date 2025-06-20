package com.jgl.TappedOut.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.Event;
import com.jgl.TappedOut.models.EventStatus;
import com.jgl.TappedOut.models.Sport;
import com.jgl.TappedOut.models.User;

/**
 * Repository interface for managing {@link Event} entities
 * Provides methods to query events based on different criteria including
 * sport, organizer, status, location and date ranges
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findBySportId(Sport sportId);

    List<Event> findByOrganizerId(User organizerId);

    List<Event> findByStatus(EventStatus status);

    List<Event> findByCountryAndCity(String country, String city);

    List<Event> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds upcoming events (end date is in the future)
     * 
     * @return list of upcoming events ordered by end date ascending
     */
    @Query("SELECT e FROM Event e WHERE " + 
        "e.startDate > CURRENT_DATE " +
        "ORDER BY e.startDate ASC")
    List<Event> findUpcomingEvents();

    /**
     * Finds past events (end date is in the past)
     * 
     * @return list of past events ordered by end date descending
     */
    @Query("SELECT e FROM Event e WHERE " +
        "e.endDate < CURRENT_DATE " +
        "ORDER BY e.endDate DESC")
    List<Event> findPastEvents();

    /**
     * Searches events with flexible criteria
     * 
     * @param sport sport filter (optional)
     * @param country country filer (optional, partial match)
     * @param city city filter (optional, partial match)
     * @param query search term for name or description (optional, partial match)
     * 
     * @return list of matching events
     */
    @Query("SELECT e FROM Event e WHERE " +
        "(:sport IS NULL OR e.sportId.id = :sport) AND " +
        "(:country IS NULL OR LOWER(e.country) LIKE LOWER(CONCAT('%', :country, '%'))) AND " +
        "(:city IS NULL OR LOWER(e.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
        "(:query IS NULL OR (LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%'))))")
    List<Event> searchEvents(
        @Param("sport") Sport sport,
        @Param("country") String country,
        @Param("city") String city,
        @Param("query") String query
    );
}