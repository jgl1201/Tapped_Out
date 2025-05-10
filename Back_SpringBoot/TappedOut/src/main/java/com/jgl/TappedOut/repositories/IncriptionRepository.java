package com.jgl.TappedOut.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
public interface IncriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByCompetitorId(User competitorId);

    List<Inscription> findByEventId(Event eventId);

    List<Inscription> findByCategoryId(Category categoryId);

    List<Inscription> findByPaymentStatus(PaymentStatus paymentStatus);

    /**
     * Finds all paid inscriptions for a specific event
     * 
     * @param eventId the event ID
     * 
     * @return list of paid inscriptions for the event
     */
    @Query("SELECT i FROM Inscription i WHERE " +
        "i.eventId.id = :eventId AND i.paymentStatus = 'PAID';")
    List<Inscription> findPaidInscriptionsByEventId(@Param("eventId") Long eventId);

    /**
     * Finds the number of paid inscriptions for a specific event
     * 
     * @param eventId the event ID
     * 
     * @return number of paid inscriptions for the event
     */
    @Query("SELECT COUNT(i) FROM Inscription i WHERE " +
        "i.eventId.id = :eventId AND i.paymentStatus = 'PAID';")
    Long countPaidInscriptionsByEventId(@Param("eventId") Long eventId);

    /**
     * Finds a specific inscription by competitor and event
     * 
     * @param userId the competitor ID
     * @param eventId the event ID
     * 
     * @return Optional containing the inscription if found
     */
    @Query("SELECT i FROM Inscription i WHERE " +
        "i.competitorId.id = :userId AND i.eventId.id = :eventId;")
    Optional<Inscription> findByCompetitorAndEvent(
        @Param("userId") Long userId,
        @Param("eventId") Long eventId
    );
}
