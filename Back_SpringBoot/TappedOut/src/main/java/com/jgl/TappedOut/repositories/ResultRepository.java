package com.jgl.TappedOut.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
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

    List<Result> findByEventIdAndCategoryId(Event eventId, Category categoryId);

    List<Result> findByEventIdAndCompetitorId(Event eventId, User competitorId);

    List<Result> findByCompetitorId(User competitorId);

    List<Result> findByEventIdAndPosition(Event eventId, int position);

    Optional<Result> findByEventIdAndCategoryIdAndPosition(Event eventId, Category categoryId, int position);

    boolean existsByEventIdAndCategoryIdAndPosition(Event eventId, Category categoryId, int position);
}