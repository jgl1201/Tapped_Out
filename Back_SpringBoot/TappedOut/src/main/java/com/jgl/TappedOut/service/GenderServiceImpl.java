package com.jgl.TappedOut.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgl.TappedOut.dto.GenderCreateDTO;
import com.jgl.TappedOut.dto.GenderResponseDTO;
import com.jgl.TappedOut.mapper.GenderMapper;
import com.jgl.TappedOut.models.Gender;
import com.jgl.TappedOut.repositories.GenderRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service class to handle logic related with {@link Gender}
 * 
 * ? Interacts with the GenderRepository in order to perform basic
 * ? CRUD operations and other specific domain operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
public class GenderServiceImpl implements GenderService {
    @Autowired
    private CustomLogger log;

    @Autowired
    private Utils utils;

    @Autowired
    private GenderRepository genderRepo;

    @Autowired
    private GenderMapper genderMapper;


    /**
     * Retrieves all genders
     * 
     * @return List of GenderResponseDTO
     */    
    @Override
    @Transactional(readOnly = true)
    public List<GenderResponseDTO> getAllGenders() {
        log.debug("Fetching all Gender");

        return genderRepo.findAll().stream()
            .map(genderMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a gender by ID
     * 
     * @param id Gender ID
     * @return GenderResponseDTO
     * @throws EntityNotFoundException if gender not found
     */
    @Override
    @Transactional(readOnly = true)
    public GenderResponseDTO getGenderById(Long id) {
        log.debug("Fetching Gender with ID: {}", id);

        Gender gender = findGenderByIdOrThrow(id);

        return genderMapper.toResponseDTO(gender);
    }

    /**
     * Retrieves a gender by NAME
     * 
     * @param name Gender NAME
     * @return GenderResponseDTO
     * @throws EntityNotFoundException if gender not found
     */
    @Override
    @Transactional(readOnly = true)
    public GenderResponseDTO getGenderByName(String name) {
        log.debug("Fetching Gender with NAME: {}", name);

        Gender gender = findGenderByNameOrThrow(name);

        return genderMapper.toResponseDTO(gender);
    }

    /**
     * Creates a gender
     * 
     * @param dto GenderCreateDTO
     * @return GenderResponseDTO
     * @throws DataIntegrityViolationException if gender already exists
     * @throws RuntimeException if failed to create gender
     */
    @Override
    @Transactional
    public GenderResponseDTO createGender(GenderCreateDTO dto) {
        log.debug("Creating Gender: {}", dto);

        validateGenderNameNotExists(dto.getName());

        try {
            Gender gender = genderMapper.fromCreateDTO(dto);
            gender = genderRepo.save(gender);
            log.info("Successfully created Gender with ID: {}", gender.getId());
            return genderMapper.toResponseDTO(gender);
        } catch (Exception e) {
            log.error("Error creating Gender: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating Gender");
        }
    }

    /**
     * Updates a gender
     * 
     * @param id Gender ID
     * @param dto GenderCreateDTO
     * @return GenderResponseDTO
     * @throws EntityNotFoundException if gender not found
     * @throws DataIntegrityViolationException if gender already exists
     * @throws RuntimeException if failed to update gender
     */
    @Override
    @Transactional
    public GenderResponseDTO updateGender(Long id, GenderCreateDTO dto) {
        log.debug("Updating Gender with ID: {}", id);

        Gender gender = findGenderByIdOrThrow(id);
        
        if(utils.hasNameChanged(gender.getName(), dto.getName())) {
            validateGenderNameNotExists(dto.getName());
        }

        try {
            genderMapper.updateFromDTO(dto, gender);
            Gender updated = genderRepo.save(gender);
            log.info("Successfully updated Gender with ID: {}", id);
            return genderMapper.toResponseDTO(updated);
        } catch (Exception e) {
            log.error("Error updating Gender: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating Gender");
        }
    }

    /**
     * Deletes a gender
     * 
     * @param id Gender ID
     * @throws EntityNotFoundException if gender not found
     * @throws RuntimeException if failed to delete gender
     */
    @Override
    @Transactional
    public void deleteGender(Long id) {
        log.info("Deleting Gender with ID: {}", id);

        findGenderByIdOrThrow(id);

        try {
            genderRepo.deleteById(id);
            log.info("Successfully deleted Gender with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting Gender with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete Gender");
        }
    }

    /**
     * Private method to find a gender by ID
     * 
     * @param id Gender ID
     * @return Gender
     * @throws EntityNotFoundException if gender not found
     */
    private Gender findGenderByIdOrThrow(Long id) {
        return genderRepo.findById(id)
            .orElseThrow(() -> {
                log.error("Gender with ID: {} not found", id);
                throw new EntityNotFoundException("Gender not found");
            });
    }

    /**
     * Private method to find a gender by NAME
     * 
     * @param name Gender NAME
     * @return Gender
     * @throws EntityNotFoundException if gender not found
     */
    private Gender findGenderByNameOrThrow(String name) {
        return genderRepo.findByName(name.trim())
            .orElseThrow(() -> {
                log.error("Gender with NAME: {} not found", name);
                throw new EntityNotFoundException("Gender not found");
            });
    }

    /**
     * Private method to validate gender does not exist by NAME
     * 
     * @param name Gender NAME
     * @throws DataIntegrityViolationException if gender already exists
     */
    private void validateGenderNameNotExists(String name) {
        if (genderRepo.findByName(name.trim()).isPresent()) {
            log.warn("Cannot create Gender - NAME '{}' already exists", name);
            throw new DataIntegrityViolationException("Gender with this name already exists");
        }
    }
}