package com.jgl.TappedOut.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.Category;
import com.jgl.TappedOut.models.Event;
import com.jgl.TappedOut.models.Result;
import com.jgl.TappedOut.models.User;

/**
 * Repository interface for managing {@link Result} entities
 * Provides methods to query competition results based on different criteria
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByEventId(Event eventId);

    List<Result> findByCategoryId(Category categoryId);

    List<Result> findByCompetitorId(User userId);

    /**
     * Finds results for a specific event and category, ordered by position
     * 
     * @param eventId the event ID
     * @param categoryId the category ID
     * 
     * @return list of results ordered by placement
     */
    @Query("SELECT r FROM Result r WHERE " +
        "r.eventId.id = :eventId AND r.categoryId.id = :categoryId " +
        "ORDER BY r.position ASC")
    List<Result> findByEventAndCategoryOrderByPosition(
        @Param("eventId") Long eventId,
        @Param("categoryId") Long categoryId
    );

    /**
     * Finds result for a specific competitor in a specific event
     * 
     * @param eventId the event ID
     * @param competitorId the competitor ID
     * 
     * @return list of results for the competitor in the event
     */
    @Query("SELECT r FROM Result r WHERE " +
        "r.eventId.id = :eventId AND r.competitorId.id = :competitorId;")
    List<Result> findByEventAndCompetitor(
    @Param("eventId") Long eventId,
    @Param("competitorId") Long competitorId
    );
}
