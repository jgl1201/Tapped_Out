package com.jgl.TappedOut.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.Category;
import com.jgl.TappedOut.models.Event;
import com.jgl.TappedOut.models.Inscription;
import com.jgl.TappedOut.models.PaymentStatus;
import com.jgl.TappedOut.models.User;

/**
 * Repository interface for managing {@link Inscription} entities
 * Provides methods to query registrations for events and categories
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    Optional<List<Inscription>> findByCompetitorId(User competitorId);

    Optional<List<Inscription>> findByEventId(Event eventId);

    Optional<List<Inscription>> findByEventIdAndCategoryId(Event eventId, Category categoryId);

    Optional<List<Inscription>> findByPaymentStatus(PaymentStatus status);

    Optional<List<Inscription>> findByEventIdAndPaymentStatus(Event eventId, PaymentStatus status);

    Optional<List<Inscription>> findByCompetitorIdAndEventId(User competitorId, Event eventId);

    Long countByEventIdAndPaymentStatus(Event eventId, PaymentStatus status);

    boolean existsByCompetitorIdAndEventId(User competitorId, Event eventId);

    boolean existsByCompetitorIdAndEventIdAndCategoryId(User competitorId, Event eventId, Category categoryId);

}