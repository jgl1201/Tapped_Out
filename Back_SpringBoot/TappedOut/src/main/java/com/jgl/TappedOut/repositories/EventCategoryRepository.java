package com.jgl.TappedOut.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.Category;
import com.jgl.TappedOut.models.Event;
import com.jgl.TappedOut.models.EventCategory;
import com.jgl.TappedOut.models.EventCategoryId;

/**
 * Repository interface for managing {@link EventCategory} entities.
 * Provides methods to query relationships between events and categories.
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, EventCategoryId> {
    Optional<EventCategory> findByEventIdAndCategoryId(Event eventId, Category categoryId);

    boolean existsByEventIdAndCategoryId(Event eventId, Category categoryId);

    /**
     * Finds all the categories available in a specific event
     * 
     * @param eventId the ID of the event to filter by
     * 
     * @return list of categories available in the event
     */
    @Query("SELECT c FROM Category c JOIN EventCategory ec " +
        "ON c.id = ec.categoryId.id WHERE " +
        "ec.eventId.id = :eventId;")
    List<Category> findCategoriesByEventId(@Param("eventId") Long eventId);
}