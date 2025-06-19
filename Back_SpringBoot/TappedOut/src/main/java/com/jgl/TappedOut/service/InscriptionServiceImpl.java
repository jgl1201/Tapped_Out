package com.jgl.TappedOut.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgl.TappedOut.dto.InscriptionCreateDTO;
import com.jgl.TappedOut.dto.InscriptionResponseDTO;
import com.jgl.TappedOut.dto.InscriptionUpdateDTO;
import com.jgl.TappedOut.mapper.InscriptionMapper;
import com.jgl.TappedOut.models.Category;
import com.jgl.TappedOut.models.Event;
import com.jgl.TappedOut.models.Inscription;
import com.jgl.TappedOut.models.PaymentStatus;
import com.jgl.TappedOut.models.User;
import com.jgl.TappedOut.repositories.InscriptionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class to handle logic related with {@link Inscription}
 * 
 * ? Interacts with the InscriptionRepository in order to perform basic
 * ? CRUD operations and other specific domain operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
@Slf4j
public class InscriptionServiceImpl implements InscriptionService {
    @Autowired
    private EmailService emailService;

    @Autowired
    private InscriptionRepository inscriptionRepo;

    @Autowired
    private InscriptionMapper inscriptionMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    @Lazy
    private EventServiceImpl eventService;

    @Autowired
    private CategoryServiceImpl categoryService;


    /**
     * Retrieves all inscriptions
     * 
     * @return List of InscriptionResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getAllInscriptions() {
        log.debug("Fetching all Inscription");

        return inscriptionRepo.findAll()
            .stream()
            .map(inscriptionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves inscription by Competitor ID
     * 
     * @param competitorId the ID of the competitor
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if referenced competitor not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getInscriptionsByCompetitor(Long competitorId) {
        log.debug("Fetching Inscription\n\tCompetitor ID: {}", competitorId);

        User competitor = userService.findUserByIdOrThrow(competitorId);

        return inscriptionRepo.findByCompetitorId(competitor)
            .stream()
            .map(inscriptionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves inscriptions by event ID
     * 
     * @param eventId the ID of the event
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if referenced event not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getInscriptionsByEvent(Long eventId) {
        log.debug("Fetching Inscription\n\tEvent ID: {}", eventId);

        Event event = eventService.findEventByIdOrThrow(eventId);

        return inscriptionRepo.findByEventId(event)
            .stream()
            .map(inscriptionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves inscriptions by category ID of an event
     * 
     * @param eventId the ID of the event
     * @param categoryId the ID of the category
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if referenced event or category not found
     * @throws EntityNotFoundException if referenced category not associated with event
     */
    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getInscriptionsByCategory(Long eventId, Long categoryId) {
        log.debug("Fetching Inscription\n\tEvent ID: {}\n\tCategory ID: {}", eventId, categoryId);

        Event event = eventService.findEventByIdOrThrow(eventId);
        Category category = categoryService.findCategoryByIdOrThrow(categoryId);

        return inscriptionRepo.findByEventIdAndCategoryId(event, category)
            .stream()
            .map(inscriptionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves inscriptions by payment status
     * 
     * @param status the payment status
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if referenced payment status not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getInscriptionByPaymentStatus(PaymentStatus status) {
        log.debug("Fetching Inscription\n\tPayment Status: {}", status);

        return inscriptionRepo.findByPaymentStatus(status)
            .stream()
            .map(inscriptionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all paid inscriptions for an event
     * 
     * @param eventId the ID of the event
     * @return List of InscriptionResponseDTO
     * @throws EntityNotFoundException if referenced event not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getPaidInscriptionsByEvent(Long eventId) {
        log.debug("Fetching PAID Inscription\n\tEvent ID: {}", eventId);

        Event event = eventService.findEventByIdOrThrow(eventId);

        return inscriptionRepo.findByEventIdAndPaymentStatus(event, PaymentStatus.PAID)
            .stream()
            .map(inscriptionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Counts all paid inscriptions for an event
     * 
     * @param eventId the ID of the event
     * @return number of paid inscriptions
     * @throws EntityNotFoundException if referenced event not found
     */
    @Override
    @Transactional(readOnly = true)
    public Long countPaidInscriptionsByEvent(Long eventId) {
        log.debug("Counting PAID Inscription\n\tEvent ID: {}", eventId);

        Event event = eventService.findEventByIdOrThrow(eventId);

        return inscriptionRepo.countByEventIdAndPaymentStatus(event, PaymentStatus.PAID);
    }

    /** 
     * Retrieves Inscription by ID
     * 
     * @param id the ID of the Inscription
     * @return InscriptionResponseDTO
     * @throws EntityNotFoundException if referenced Inscription not found
     */
    @Override
    @Transactional(readOnly = true)
    public InscriptionResponseDTO getInscriptionById(Long id) {
        log.debug("Fetching Inscription with ID: {}", id);

        Inscription inscription = findInscriptionByIdOrThrow(id);

        return inscriptionMapper.toResponseDTO(inscription);
    }

    /**
     * Retrieves Inscriptions by Competitor and Event
     * 
     * @param competitorId the ID of the competitor
     * @param eventId the ID of the event
     * @return List of InscriptionResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getInscriptionByCompetitorAndEvent(Long competitorId, Long eventId) {
        log.debug("Fetching Inscription\n\tCompetitor ID: {}\n\tEvent ID: {}", competitorId, eventId);

        User competitor = userService.findUserByIdOrThrow(competitorId);
        Event event = eventService.findEventByIdOrThrow(eventId);

        return inscriptionRepo.findByCompetitorIdAndEventId(competitor, event)
            .stream()
            .map(inscriptionMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Creates an Inscription
     * 
     * @param dto the InscriptionCreateDTO
     * @return InscriptionResponseDTO
     * @throws EntityNotFoundException if referenced competitor or event not found
     */
    @Override
    @Transactional
    public InscriptionResponseDTO createInscription(InscriptionCreateDTO dto) {
        log.debug("Creating Inscription\n\tCompetitor ID: {}\n\tEvent ID: {}",
            dto.getCompetitorId(), dto.getEventId());

        User competitor = userService.findUserByIdOrThrow(dto.getCompetitorId());
        Event event = eventService.findEventByIdOrThrow(dto.getEventId());
        Category category = categoryService.findCategoryByIdOrThrow(dto.getCategoryId());

        validateUserNotInscribedAtEvent(competitor, event);
        validateCompetitorCategoryMatch(competitor, category);
        
        try {
            Inscription inscription = inscriptionMapper.fromCreateDTO(dto);
            inscription = inscriptionRepo.save(inscription);
            log.info("Successfully created Inscription with ID: {}", inscription.getId());

            emailService.sendEventInscriptionEmail(
                competitor.getEmail().trim().toLowerCase(),
                event.getName().trim().toUpperCase(),
                event.getStartDate().toString());

            return inscriptionMapper.toResponseDTO(inscription);
        } catch(Exception e) {
            log.error("Error creating Inscription: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create Inscription");
        }
    }

    /**
     * Updates an Inscription
     * 
     * @param id the ID of the Inscription
     * @param dto the InscriptionUpdateDTO
     * @return InscriptionResponseDTO
     * @throws EntityNotFoundException if referenced Inscription not found
     */
    @Override
    @Transactional
    public InscriptionResponseDTO updateInscription(Long id, InscriptionUpdateDTO dto) {
        log.debug("Updating Inscription with ID: {}", id);

        Inscription inscription = findInscriptionByIdOrThrow(id);
        User competitor = inscription.getCompetitorId();
        Category category = categoryService.findCategoryByIdOrThrow(dto.getCategoryId());

        validateUserNotInscribedAtEvent(competitor, inscription.getEventId());
        validateCompetitorCategoryMatch(competitor, category);

        try {
            inscriptionMapper.updateFromDTO(dto, inscription);
            Inscription updatedInscription = inscriptionRepo.save(inscription);
            log.info("Successfully updated Inscription with ID: {}", updatedInscription.getId());
            return inscriptionMapper.toResponseDTO(updatedInscription);
        } catch(Exception e) {
            log.error("Error updating Inscription: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update Inscription");
        }
    }

    /**
     * Deletes an Inscription
     * 
     * @param id the ID of the Inscription
     * @throws EntityNotFoundException if referenced Inscription not found
     */
    @Override
    @Transactional
    public void deleteInscription(Long id) {
        log.debug("Deleting Inscription with ID: {}", id);

        findInscriptionByIdOrThrow(id);

        try {
            inscriptionRepo.deleteById(id);
            log.info("Successfully deleted Inscription with ID: {}", id);
        } catch(Exception e) {
            log.error("Error deleting Inscription with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete Inscription");
        }
    }

    /**
     * Method to find an Inscription by ID
     * 
     * @param id the ID of the Inscription
     * @return Inscription
     * @throws EntityNotFoundException if referenced Inscription not found
     */
    public Inscription findInscriptionByIdOrThrow(Long id) {
        return inscriptionRepo.findById(id)
            .orElseThrow(() -> {
                log.error("Inscription not found (ID: {})", id);
                throw new EntityNotFoundException("Inscription not found with ID: " + id);
            });
    }

    /**
     * Method to find an Inscription by Competitor and Event
     * 
     * @param competitor the Competitor
     * @param event the Event
     * @return Inscription
     * @throws EntityNotFoundException if referenced Inscription not found
     */
    public List<Inscription> findInscriptionByCompetitorAndEventOrThrow(User competitor, Event event) {
        return inscriptionRepo.findByCompetitorIdAndEventId(competitor, event);
    }

    /**
     * Method to validate user not inscribed at event
     * 
     * @param user the User
     * @param event the Event
     * @throws IllegalArgumentException if user already inscribed at event
     */
    public void validateUserNotInscribedAtEvent(User user, Event event) {
        if (inscriptionRepo.existsByCompetitorIdAndEventId(user, event))
            throw new IllegalArgumentException("User already inscribed at event");
    }

    /**
     * Method to validate a competitor is compatible with the category
     * 
     * @param competitor the Competitor
     * @param category the Category
     * @throws IllegalArgumentException if competitor is not compatible with category
     */
    public void validateCompetitorCategoryMatch(User competitor, Category category) {
        if (!competitor.getGenderId().equals(category.getGenderId()))
            throw new IllegalArgumentException("Competitor GENDER is not compatible with category");

        int age = Period.between(competitor.getDateOfBirth(), LocalDate.now()).getYears();

        if (age < category.getMinAge() || age > category.getMaxAge())
            throw new IllegalArgumentException("Competitor AGE is not compatible with category");
    }
}