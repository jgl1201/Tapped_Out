package com.jgl.TappedOut.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgl.TappedOut.dto.SportLevelCreateDTO;
import com.jgl.TappedOut.dto.SportLevelResponseDTO;
import com.jgl.TappedOut.dto.SportLevelUpdateDTO;
import com.jgl.TappedOut.mapper.SportLevelMapper;
import com.jgl.TappedOut.models.Sport;
import com.jgl.TappedOut.models.SportLevel;
import com.jgl.TappedOut.repositories.SportLevelRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class to handle logic related with {@link SportLevel}
 * 
 * ? Interacts with the SportLevelRepository in order to perform basic
 * ? CRUD operations and other specific domain operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
@Slf4j
public class SportLevelServiceImpl implements SportLevelService {
    @Autowired
    private Utils utils;

    @Autowired
    private SportLevelRepository sportLevelRepo;

    @Autowired
    private SportLevelMapper sportLevelMapper;

    @Autowired
    private SportServiceImpl sportService;


    /**
     * Retrieves all sport levels
     * 
     * @return List of SportLevelResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<SportLevelResponseDTO> getAllSportLevels() {
        log.debug("Fetching all SportLevel");

        return sportLevelRepo.findAll()
            .stream()
            .map(sportLevelMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves sport levels by sport ID
     * 
     * @param sportId Sport ID
     * @return List of SportLevelResponseDTO
     * @throws EntityNotFoundException if sport not found from SportService
     */
    @Override
    @Transactional(readOnly = true)
    public List<SportLevelResponseDTO> getSportLevelsBySport(Long sportId) {
        log.debug("Fetching SportLevel\n\tSport ID: {}", sportId);
        
        Sport sport = sportService.findSportByIdOrThrow(sportId);

        return sportLevelRepo.findBySportId(sport)
            .stream()
            .map(sportLevelMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a sport level by ID
     * 
     * @param id SportLevel ID
     * @return SportLevelResponseDTO
     * @throws EntityNotFoundException if sport level not found
     */
    @Override
    @Transactional(readOnly = true)
    public SportLevelResponseDTO getSportLevelById(Long id) {
        log.debug("Fetching SportLevel with ID: {}", id);

        SportLevel sportLevel = findSportLevelByIdOrThrow(id);

        return sportLevelMapper.toResponseDTO(sportLevel);
    }

    /**
     * Retrieves a sport level by sport ID and level NAME
     * 
     * @param sportId Sport ID
     * @param name SportLevel NAME
     * @return SportLevelResponseDTO
     * @throws EntityNotFoundException if sport not found from SportService
     * @throws EntityNotFoundException if sport level not found
     */
    @Override
    @Transactional(readOnly = true)
    public SportLevelResponseDTO getSportLevelBySportAndName(Long sportId, String name) {
        log.debug("Fetching SportLevel\n\tSport ID: {}\n\tNAME: {}", sportId, name);

        SportLevel sportLevel = findSportLevelBySportAndNameOrThrow(sportId, name);

        return sportLevelMapper.toResponseDTO(sportLevel);
    }

    /**
     * Creates a new sport level
     * 
     * @param dto SportLevelCreateDTO
     * @return Created SportLevelResponseDTO
     * @throws EntityNotFoundException if sport not found from SportService
     * @throws DataIntegrityViolationException if sport level name already exists for the sport
     * @throws RuntimeException if failed to create sport level
     */
    @Override
    @Transactional
    public SportLevelResponseDTO createSportLevel(SportLevelCreateDTO dto) {
        log.info("Creating new SportLevel with NAME: {} \n\tSport ID: {}", dto.getName().trim(), dto.getSportId());

        Sport sport = sportService.findSportByIdOrThrow(dto.getSportId());
        
        validateSportLevelNameNotExists(sport, dto.getName());

        try {
            SportLevel sportLevel = sportLevelMapper.fromCreateDTO(dto);
            sportLevel = sportLevelRepo.save(sportLevel);
            log.info("Successfully created SportLevel with ID: {} for Sport ID: {}", sportLevel.getId(), dto.getSportId());
            return sportLevelMapper.toResponseDTO(sportLevel);
        } catch (Exception e) {
            log.error("Error creating SportLevel: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create sport level");
        }
    }

    /**
     * Updates an existing sport level
     * 
     * @param id SportLevel ID
     * @param dto SportLevelUpdateDTO
     * @return Updated SportLevelResponseDTO
     * @throws EntityNotFoundException if sport level not found
     * @throws EntityNotFoundException if sport not found from SportService
     * @throws DataIntegrityViolationException if new name already exists for the sport
     * @throws RuntimeException if failed to update sport level
     */
    @Override
    @Transactional
    public SportLevelResponseDTO updateSportLevel(Long id, SportLevelUpdateDTO dto) {
        log.info("Updating SportLevel with ID: {}", id);

        Sport sport = sportService.findSportByIdOrThrow(dto.getSportId());
        SportLevel sportLevel = findSportLevelByIdOrThrow(id);
        
        if (utils.hasNameChanged(sportLevel.getName(), dto.getName()) || isSportChanged(sportLevel, dto.getSportId())) {
            validateSportLevelNameNotExists(sport, dto.getName());
        }

        try {
            sportLevelMapper.updateFromDTO(dto, sportLevel);
            SportLevel updatedSportLevel = sportLevelRepo.save(sportLevel);
            log.info("Successfully updated SportLevel with ID: {}", id);
            return sportLevelMapper.toResponseDTO(updatedSportLevel);
        } catch (Exception e) {
            log.error("Error updating SportLevel with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update SportLevel");
        }
    }

    /**
     * Deletes a sport level by ID
     * 
     * @param id SportLevel ID
     * @throws EntityNotFoundException if sport level not found
     * @throws RuntimeException if failed to delete sport level
     */
    @Override
    @Transactional
    public void deleteSportLevel(Long id) {
        log.info("Deleting SportLevel with ID: {}", id);

        findSportLevelByIdOrThrow(id);

        try {
            sportLevelRepo.deleteById(id);
            log.info("Successfully deleted SportLevel with ID: {}", id); 
        } catch (Exception e) {
            log.error("Error deleting SportLevel with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete SportLevel");
        }
    }

    

    /**
     * Method to find a sport level by ID
     * 
     * @param id SportLevel ID
     * @return SportLevel
     * @throws EntityNotFoundException if sport level not found
     */
    public SportLevel findSportLevelByIdOrThrow(Long id) {
        return sportLevelRepo.findById(id)
            .orElseThrow(() -> {
                log.error("SportLevel with ID: {} not found", id);
                throw new EntityNotFoundException("SportLevel not found");
            });
    }

    /**
     * Method to find a sport level by sport ID and level NAME
     * 
     * @param sportId Sport ID
     * @param name SportLevel NAME
     * @return SportLevel
     * @throws EntityNotFoundException if sport not found from SportService
     * @throws EntityNotFoundException if sport level not found
     */
    public SportLevel findSportLevelBySportAndNameOrThrow(Long sportId, String name) {
        Sport sport = sportService.findSportByIdOrThrow(sportId);

        return sportLevelRepo.findBySportIdAndName(sport, name.trim())
            .orElseThrow(() -> {
                log.error("SportLevel with Sport ID: {} and NAME: {} not found", sportId, name);
                throw new EntityNotFoundException("SportLevel not found");
            });
    }

    /**
     * Mehtod to validate a sport level does not exist by NAME
     * 
     * @param name SportLevel NAME
     * @throws DataIntegrityViolationException if sport level already exists
     */
    public void validateSportLevelNameNotExists(Sport sport, String name) {
        if (sportLevelRepo.findBySportIdAndName(sport, name.trim()).isPresent()) {
            log.warn("SportLevel with NAME: '{}' already exists for Sport: {}", name, sport.getId());
            throw new DataIntegrityViolationException("SportLevel with this name already exists for the selected sport");
        }
    }

    /**
     * Method to check if a sport level has changed its sport
     * 
     * @param sportLevel SportLevel
     * @param newSportId New sport ID
     * @return boolean
     */
    public boolean isSportChanged(SportLevel sportLevel, Long newSportId) {
        return !sportLevel.getSportId().getId().equals(newSportId);
    }
}