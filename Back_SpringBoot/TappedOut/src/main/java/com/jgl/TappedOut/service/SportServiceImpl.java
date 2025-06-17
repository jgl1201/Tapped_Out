package com.jgl.TappedOut.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgl.TappedOut.dto.SportCreateDTO;
import com.jgl.TappedOut.dto.SportResponseDTO;
import com.jgl.TappedOut.mapper.SportMapper;
import com.jgl.TappedOut.models.Sport;
import com.jgl.TappedOut.repositories.SportRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class to handle logic related with {@link Sport}
 * 
 * ? Interacts with the SportRepository in order to perform basic
 * ? CRUD operations and other specific domain operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
@Slf4j
public class SportServiceImpl implements SportService {
    @Autowired
    private Utils utils;

    @Autowired
    private SportRepository sportRepo;

    @Autowired
    private SportMapper sportMapper;


    /**
     * Retrieves all sports
     * 
     * @return a list of SportResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<SportResponseDTO> getAllSports() {
        log.debug("Fetching all Sport");

        return sportRepo.findAll()
            .stream()
            .map(sportMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a sport by ID
     * 
     * @param id Sport ID
     * @return SportResponseDTO
     * @throws EntityNotFoundException if sport not found
     */
    @Override
    @Transactional(readOnly = true)
    public SportResponseDTO getSportById(Long id) {
        log.debug("Fetching Sport with ID: {}", id);

        Sport sport = findSportByIdOrThrow(id);
        
        return sportMapper.toResponseDTO(sport);
    }

    /**
     * Retrieves a sport by NAME
     * 
     * @param name Sport NAME
     * @return SportResponseDTO
     * @throws EntityNotFoundException if sport not found
     */
    @Override
    @Transactional(readOnly = true)
    public SportResponseDTO getSportByName(String name) {
        log.debug("Fetching Sport with NAME: {}", name);

        Sport sport = findSportByNameOrThrow(name);

        return sportMapper.toResponseDTO(sport);
    }

    /**
     * Creates a new sport
     * 
     * @param dto SportCreateDTO
     * @return SportResponseDTO
     * @throws DataIntegrityViolationException if sport already exists
     * @throws RuntimeException if failed to create sport
     */
    @Override
    @Transactional
    public SportResponseDTO createSport(SportCreateDTO dto) {
        log.info("Creating new Sport: {}", dto);

        validateSportNameNotExists(dto.getName());

        try {
            Sport sport = sportMapper.fromCreateDTO(dto);
            sport = sportRepo.save(sport);
            log.info("Successfully created Sport with ID: {}", sport.getId());
            return sportMapper.toResponseDTO(sport);
        } catch(Exception e) {
            log.error("Error creating Sport: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create sport");
        }
    }

    /**
     * Updates a sport
     * 
     * @param id Sport ID
     * @param dto SportCreateDTO
     * @return SportResponseDTO
     * @throws EntityNotFoundException if sport not found
     * @throws DataIntegrityViolationException if sport already exists
     * @throws RuntimeException if failed to update sport
     */
    @Override
    @Transactional
    public SportResponseDTO updateSport(Long id, SportCreateDTO dto) {
        log.info("Updating Sport with ID:  {}", id);

        Sport sport = findSportByIdOrThrow(id);
        
        if (utils.hasNameChanged(sport.getName(), dto.getName())) {
            validateSportNameNotExists(dto.getName());
        }

        try {
            sportMapper.updateFromDTO(dto, sport);
            Sport updated = sportRepo.save(sport);
            log.info("Successfully updated Sport with ID: {}", id);
            return sportMapper.toResponseDTO(updated);
        } catch(Exception e) {
            log.error("Error updating Sport: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update sport", e);
        }
    }

    /**
     * Deletes a sport
     * 
     * @param id Sport ID
     * @throws EntityNotFoundException if sport not found
     * @throws RuntimeException if failed to delete sport
     */
    @Override
    @Transactional
    public void deleteSport(Long id) {
        log.info("Deleting Sport with ID: {}", id);

        findSportByIdOrThrow(id);

        try {
            sportRepo.deleteById(id);
            log.info("Successfully deleted Sport with ID: {}", id);
        } catch(Exception e) {
            log.error("Error deleting Sport with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete Sport", e);
        }
    }

    /**
     * Method to find a sport by ID
     * 
     * @param id Sport ID
     * @return Sport
     * @throws EntityNotFoundException if sport not found
     */
    public Sport findSportByIdOrThrow(Long id) {
        return sportRepo.findById(id)
            .orElseThrow(() -> {
                log.error("Sport with ID: {} not found", id);
                throw new EntityNotFoundException("Sport not found");
            });
    }

    /**
     * Method to find a sport by NAME
     * 
     * @param name Sport NAME
     * @return Sport
     * @throws EntityNotFoundException if sport not found
     */
    public Sport findSportByNameOrThrow(String name) {
        return sportRepo.findByName(name.trim())
            .orElseThrow(() -> {
                log.error("Sport with NAME: {} not found", name);
                throw new EntityNotFoundException("Sport not found");
            });
    }

    /**
     * Method to validate sport does not exist by NAME
     * 
     * @param name Sport NAME
     * @throws DataIntegrityViolationException if sport already exists
     */
    public void validateSportNameNotExists(String name) {
        if (sportRepo.findByName(name.trim()).isPresent()) {
            log.warn("Cannot create Sport - NAME '{}' already exists", name);
            throw new DataIntegrityViolationException("Sport with this name already exists");
        }
    }
}