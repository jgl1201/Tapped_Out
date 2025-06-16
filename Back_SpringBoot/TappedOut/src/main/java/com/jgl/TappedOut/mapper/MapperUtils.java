package com.jgl.TappedOut.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jgl.TappedOut.models.*;
import com.jgl.TappedOut.repositories.*;

import jakarta.persistence.EntityNotFoundException;

/**
 * Utility class with common mapper operations
 * 
 * @author Jorge García López
 * @version 1.0.7
 * @since 2025
 */
@Component
public class MapperUtils {
    // * Repositories' injection

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private GenderRepository genderRepo;

    @Autowired
    private InscriptionRepository inscriptionRepo;
    
    @Autowired
    private SportRepository sportRepo;

    @Autowired
    private SportLevelRepository sportLevelRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserTypeRepository userTypeRepo;

    // * Password Encoder from Spring Security
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // * Finding methods

    /**
     * Helper method to find {@link Category} by ID
     * ? Uses {@link CategoryRepository}
     * 
     * @param categoryId the id to find by
     * @return the category found if any
     * @throws EntityNotFoundException if category not found
     */
    public Category mapCategory(Long categoryId) {
        if (categoryId == null) return null;

        return categoryRepo.findById(categoryId)
            .orElseThrow(() -> new EntityNotFoundException("Category with ID: " + categoryId + " not found"));
    }

    /**
     * Helper method to find {@link Event} by ID
     * ? Uses {@link EventRepository}
     * 
     * @param eventId the id to find by
     * @return the event found if any
     * @throws EntityNotFoundException if event not found
     */
    public Event mapEvent(Long eventId) {
        if (eventId == null) return null;

        return eventRepo.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event with ID: " + eventId + " not found"));
    }

    /**
     * Helper method to find a {@link Gender} by ID
     * ? Uses {@link GenderRepository}
     * 
     * @param genderId the id to find by
     * @return the gender found if any
     * @throws EntityNotFoundException if gender not found
     */
    protected Gender mapGender(Long genderId) {
        if (genderId == null) return null;

        return genderRepo.findById(genderId)
            .orElseThrow(() -> new EntityNotFoundException("Gender with ID: " + genderId + " not found"));
    }

    /**
     * Helper method to find a {@link Sport} by ID
     * ? Uses {@link SportRepository}
     * 
     * @param sportId the id to find by
     * @return the sport found if any
     * @throws EntityNotFoundException if sport not found
     */
    public Sport mapSport(Long sportId) {
        if (sportId == null) return null;

        return sportRepo.findById(sportId)
            .orElseThrow(() -> new EntityNotFoundException("Sport with ID: " + sportId + " not found"));
    }

    /**
     * Helper method to find a {@link SportLevel} by ID
     * A SportLevel can be null
     * ? Uses {link SportLevelRepository}
     * 
     * @param levelId the id to find by
     * @return the sport level found if any
     * @throws EntityNotFoundException if sport level not found
     */
    protected SportLevel mapLevel(Long levelId) {
        if (levelId == null) return null;

        return sportLevelRepo.findById(levelId)
            .orElseThrow(() -> new EntityNotFoundException("Sport Level with ID: " + levelId + " not found"));
    }

    /**
     * Helper method to find a {@link User} by ID
     * ? Uses {@link UserRepository}
     * 
     * @param userId the id to find by
     * @return the user found if any
     * @throws EntityNotFoundException if user not found
     */
    public User mapUser(Long userId) {
        if (userId == null) return null;

        return userRepo.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User with ID: " + userId + " not found"));
    }

    /**
     * Helper methos to find a {@link UserType} by ID
     * ? Uses {@link UserTypeRepository}
     * 
     * @param typeId the id to find by
     * @return the user type found if any
     * @throws EntityNotFoundException is user type not found
     */
    public UserType mapUserType(Long typeId) {
        if (typeId == null) return null;

        return userTypeRepo.findById(typeId)
            .orElseThrow(() -> new EntityNotFoundException("User type with ID: " + typeId + " not found"));
    }

    // * Validation methods

    /**
     * Validates age ranges
     */
    public void validateAgeRanges(Integer minAge, Integer maxAge) {
        if (minAge != null && maxAge != null && minAge > maxAge)
            throw new IllegalArgumentException("Min age cannot be greater than max age");
    }

    /**
     * Validates date ranges
     */
    public void validateDateRanges(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null){
            if (startDate.isAfter(endDate))
                throw new IllegalArgumentException("Start date cannot be after end date");
            
            if (startDate.isBefore(LocalDateTime.now()))
                throw new IllegalArgumentException("Dates must be in the future");
        }
    }

    /**
     * Validates Event's registration fee
     */
    public void validateRegistrationFee(BigDecimal registerFee) {
        if (registerFee == null) return;

        if (registerFee != null && registerFee.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Registration fee cannot be negative");
    }

    /**
     * Validates weight ranges
     */
    public void validateWeightRanges(BigDecimal minWeight, BigDecimal maxWeight) {
        if (minWeight != null && maxWeight != null && minWeight.compareTo(maxWeight) > 0)
            throw new IllegalArgumentException("Min weight cannot be grater than max weight");
    }

    /**
     * Validates category belonging to a event's sport
     */
    public void validateCategoryEventMatch(Category category, Event event) {
        if (!category.getSportId().getId().equals(event.getSportId().getId()))
            throw new IllegalArgumentException("Category does not belong to event's sport");
    }

    /**
     * Validates inscription creation/update
     * ! Can't create 3 days before the event start
     */
    public void validateInscription(LocalDateTime eventStartDate) {
        if (LocalDateTime.now().isAfter(eventStartDate))
            throw new IllegalStateException("Cannot create/update inscription after the event start");

        if (!LocalDateTime.now().isBefore(eventStartDate.minusDays(3)))
            throw new IllegalStateException("Cannot create/update inscription 3 days before the event start");

    }

    /**
     * Validates inscription cancellation rules
     * Can be cancelled by
     *  - Cancelling payment
     *  - 1 day before the event start
     */
    public void validateInscriptionCancellation(LocalDateTime registerDate, LocalDateTime eventStartDate, PaymentStatus status) {
        if (status == PaymentStatus.CANCELLED && LocalDateTime.now().isAfter(eventStartDate.minusDays(1)))
            throw new IllegalStateException("Cannot cancel inscription within 24 hours before event start");
    }

    /**
     * Validates result's position
     */
    public void validatePosition(Integer position) {
        if (position != null && position <= 0)
            throw new IllegalArgumentException("Position must be positive number");
    }

    /**
     * Validates competitor is registered for event/category
     * in order to register a result
     */
    public void validateResultRegister(User competitorId, Event eventId, Category categoryId) {
        if (!inscriptionRepo.existsByCompetitorIdAndEventIdAndCategoryId(competitorId, eventId, categoryId))
            throw new IllegalArgumentException("Competitor is not registered for this event/category");
    }

    /**
     * Validates the result is registered after the event starts
     */
    public void validateResultTiming(LocalDateTime eventStartDate) {
        if (LocalDateTime.now().isBefore(eventStartDate))
            throw new IllegalStateException("Cannot register a result before event starts");
    }

    /**
     * Validates a SportLevel existing by name
     */
    public void validateUniqueName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Sport level name cannot be blank");
        
        if (sportLevelRepo.existsByNameIgnoreCase(name))
            throw new IllegalArgumentException("A sport level with the name: " + name + " already exists");
    }

    /**
     * Validates User's unique fields
     */
    public void validateUserUniqueFields(String dni, String email) {
        if (userRepo.existsByDniIgnoreCase(dni))
            throw new IllegalArgumentException("DNI: " + dni + " already exists");

        if (userRepo.existsByEmailIgnoreCase(email))
            throw new IllegalArgumentException("Email: " + email + " already exists");
    }

    // TODO: Research and extract into a bean, then inject that bean here and manage encodig logic
    public String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
}