package com.jgl.TappedOut.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgl.TappedOut.dto.ResultCreateDTO;
import com.jgl.TappedOut.dto.ResultResponseDTO;
import com.jgl.TappedOut.dto.ResultUpdateDTO;
import com.jgl.TappedOut.mapper.ResultMapper;
import com.jgl.TappedOut.models.Category;
import com.jgl.TappedOut.models.Event;
import com.jgl.TappedOut.models.Result;
import com.jgl.TappedOut.models.User;
import com.jgl.TappedOut.repositories.InscriptionRepository;
import com.jgl.TappedOut.repositories.ResultRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class to handle logic related with {@link Result}
 * 
 * ? Interacts with the ResultRepository and other services to perform
 * ? CRUD operations and other specific domain operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
@Slf4j
public class ResultServiceImpl implements ResultService {
    @Autowired
    private ResultRepository resultRepo;

    @Autowired
    private ResultMapper resultMapper;

    @Autowired
    private EventServiceImpl eventService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private InscriptionRepository inscriptionRepo;


    /**
     * Retrieves all Results
     * 
     * @return List of ResultResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResultResponseDTO> getAllResults() {
        log.debug("Fetching all Result");

        return resultRepo.findAll().stream()
            .map(resultMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all Results of an Event
     * 
     * @param eventId the ID of the event
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if referenced event not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResultResponseDTO> getResultsByEvent(Long eventId) {
        log.debug("Fetching Result\n\tEvent ID: {}", eventId);

        Event event = eventService.findEventByIdOrThrow(eventId);

        return resultRepo.findByEventId(event)
            .orElseThrow(() -> new EntityNotFoundException("Inscription not found for Event ID: " + eventId))
            .stream()
            .map(resultMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all Results of an Event and Category
     * 
     * @param eventId the ID of the event
     * @param categoryId the ID of the category
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if referenced event or category not found
     * @throws EntityNotFoundException if referenced category not associated with event
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResultResponseDTO> getResultsByEventAndCategory(Long eventId, Long categoryId) {
        log.debug("Fetching Result\n\tEvent ID: {}\n\tCategory ID: {}", eventId, categoryId);

        Event event = eventService.findEventByIdOrThrow(eventId);
        Category category = categoryService.findCategoryByIdOrThrow(categoryId);

        return resultRepo.findByEventIdAndCategoryId(event, category)
            .orElseThrow(() -> new EntityNotFoundException("Inscription not found for Event ID: " + eventId + " and Category ID: " + categoryId))
            .stream()
            .map(resultMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all Results of a Competitor at an Event
     * 
     * @param competitorId the ID of the competitor
     * @param eventId the ID of the event
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if referenced competitor or event not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResultResponseDTO> getResultsByEventAndCompetitor(Long eventId, Long competitorId) {
        log.debug("Fetching Result\n\tCompetitor ID: {}\n\tEvent ID: {}", competitorId, eventId);

        User competitor = userService.findUserByIdOrThrow(competitorId);
        Event event = eventService.findEventByIdOrThrow(eventId);

        return resultRepo.findByEventIdAndCompetitorId(event, competitor)
            .orElseThrow(() -> new EntityNotFoundException("Inscription not found for Competitor ID: " + competitorId + " and Event ID: " + eventId))
            .stream()
            .map(resultMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all Results of a Competitor
     * 
     * @param competitorId the ID of the competitor
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if referenced competitor not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResultResponseDTO> getResultsByCompetitor(Long competitorId) {
        log.debug("Fetching Result\n\tCompetitor ID: {}", competitorId);

        User competitor = userService.findUserByIdOrThrow(competitorId);

        return resultRepo.findByCompetitorId(competitor)
            .orElseThrow(() -> new EntityNotFoundException("Inscription not found for Competitor ID: " + competitorId))
            .stream()
            .map(resultMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all Results for a Position at an Event
     * 
     * @param eventId the ID of the event
     * @param position the position
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if referenced position not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResultResponseDTO> getResultsByEventAndPosition(Long eventId, Integer position) {
        log.debug("Fetching Result\n\tEvent ID: {}\n\tPosition: {}", eventId, position);

        Event event = eventService.findEventByIdOrThrow(eventId);

        return resultRepo.findByEventIdAndPosition(event, position)
            .orElseThrow(() -> new EntityNotFoundException("Position not found"))
            .stream()
            .map(resultMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a Result by ID
     * 
     * @param id the ID of the Result
     * @return ResultResponseDTO
     * @throws EntityNotFoundException if referenced Result not found
     */
    @Override
    @Transactional(readOnly = true)
    public ResultResponseDTO getResultById(Long id) {
        log.debug("Fetching Result with ID: {}", id);

        Result result = findResultByIdOrThrow(id);

        return resultMapper.toResponseDTO(result);
    }

    /**
     * Retrieves winners of Category at an Event
     * 
     * @param eventId the ID of the event
     * @param categoryId the ID of the category
     * @return List of ResultResponseDTO
     * @throws EntityNotFoundException if referenced event not found
     */
    @Override
    @Transactional(readOnly = true)
    public ResultResponseDTO getWinnerByEventAndCategory(Long eventId, Long categoryId) {
        log.debug("Fetching Winners\n\tEvent ID: {}\n\tCategory ID: {}", eventId, categoryId);

        Event event = eventService.findEventByIdOrThrow(eventId);
        Category category = categoryService.findCategoryByIdOrThrow(categoryId);

        return resultRepo.findByEventIdAndCategoryIdAndPosition(event, category, 1)
            .map(resultMapper::toResponseDTO)
            .orElseThrow(() -> new EntityNotFoundException("Winners not found for Event ID: " + event + " and Category ID: " + category));
    }

    /**
     * Creates a new Result
     * 
     * @param dto the ResultCreateDTO
     * @return ResultResponseDTO
     * @throws EntityNotFoundException if referenced event or competitor not found
     */
    @Override
    @Transactional
    public ResultResponseDTO createResult(ResultCreateDTO dto) {
        log.debug("Creating Result\n\tEvent ID: {}\n\tCategory ID: {}\n\tCompetitor ID: {}\n\tPosition: {}",
            dto.getEventId(), dto.getCategoryId(), dto.getCompetitorId(), dto.getPosition());

        Event event = eventService.findEventByIdOrThrow(dto.getEventId());
        User competitor = userService.findUserByIdOrThrow(dto.getCompetitorId());
        Category category = categoryService.findCategoryByIdOrThrow(dto.getCategoryId());

        validateUserInscribedAtEventCategory(competitor, event, category);
        validatePositionUniqueAtCategory(event, category, dto.getPosition());

        try {
            Result result = resultMapper.fromCreateDTO(dto);
            result = resultRepo.save(result);
            log.info("Successfully created Result with ID: {}", result.getId());
            return resultMapper.toResponseDTO(result);
        } catch(Exception e) {
            log.error("Error creating Result: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create Result");
        }
    }

    /**
     * Updates a Result
     * 
     * @param id the ID of the Result
     * @param dto the ResultUpdateDTO
     * @return ResultResponseDTO
     * @throws EntityNotFoundException if referenced Result not found
     */
    @Override
    @Transactional
    public ResultResponseDTO updateResult(Long id, ResultUpdateDTO dto) {
        log.debug("Updating Result with ID: {}", id);

        Result result = findResultByIdOrThrow(id);
        Event event = eventService.findEventByIdOrThrow(result.getEventId().getId());
        User competitor = userService.findUserByIdOrThrow(result.getCompetitorId().getId());
        Category category = categoryService.findCategoryByIdOrThrow(result.getCategoryId().getId());

        validateUserInscribedAtEventCategory(competitor, event, category);
        validatePositionUniqueAtCategory(event, category, dto.getPosition());

        try {
            resultMapper.updateFromDTO(dto, result);
            result = resultRepo.save(result);
            log.info("Successfully updated Result with ID: {}", result.getId());
            return resultMapper.toResponseDTO(result);
        } catch(Exception e) {
            log.error("Error updating Result: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update Result");
        }
    }

    /**
     * Deletes a Result
     * 
     * @param id the ID of the Result
     * @throws EntityNotFoundException if referenced Result not found
     */
    @Override
    @Transactional
    public void deleteResult(Long id) {
        log.debug("Deleting Result with ID: {}", id);

        findResultByIdOrThrow(id);

        try {
            resultRepo.deleteById(id);
            log.info("Successfully deleted Result with ID: {}", id);
        } catch(Exception e) {
            log.error("Error deleting Result: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete Result");
        }
    }

    /**
     * Method to find a Result by ID
     * 
     * @param id the ID of the Result
     * @return Result
     * @throws EntityNotFoundException if referenced Result not found
     */
    public Result findResultByIdOrThrow(Long id) {
        return resultRepo.findById(id)
            .orElseThrow(() -> {
                log.error("Result not found (ID: {})", id);
                throw new EntityNotFoundException("Result not found with ID: " + id);
            });
    }

    /**
     * Method to validate that a competitor was inscribed at an
     * event's category before resulting
     * 
     * @param competitor the Competitor
     * @param event the Event
     * @param category the Category
     * @throws IllegalArgumentException if competitor was not inscribed at event's category
     */
    public void validateUserInscribedAtEventCategory(User competitor, Event event, Category category) {
        if (!inscriptionRepo.existsByCompetitorIdAndEventIdAndCategoryId(competitor, event, category))
            throw new IllegalArgumentException("User was not inscribed at event's category");
    }

    /**
     * Method to validate a position is unique at a category of an event
     * 
     * @param event the Event
     * @param category the Category
     * @param position the Position
     * @throws IllegalArgumentException if position is not unique at category of event
     */
    public void validatePositionUniqueAtCategory(Event event, Category category, Integer position) {
        if (resultRepo.existsByEventIdAndCategoryIdAndPosition(event, category, position))
            throw new IllegalArgumentException("Position is not unique at category of event");
    }
}