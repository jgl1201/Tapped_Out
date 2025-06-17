package com.jgl.TappedOut.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgl.TappedOut.dto.UserTypeCreateDTO;
import com.jgl.TappedOut.dto.UserTypeResponseDTO;
import com.jgl.TappedOut.mapper.UserTypeMapper;
import com.jgl.TappedOut.models.UserType;
import com.jgl.TappedOut.repositories.UserTypeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class to handle logic related with {@link UserType}
 * 
 * ? Interacts with the UserTypeRepository in order to perform basic
 * ? CRUD operations and other specific domain operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
@Slf4j
public class UserTypeServiceImpl implements UserTypeService {
    @Autowired
    private Utils utils;

    @Autowired
    private UserTypeRepository userTypeRepo;

    @Autowired
    private UserTypeMapper userTypeMapper;


    /**
     * Retrieves all user types
     * 
     * @return a list of UserTypeResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserTypeResponseDTO> getAllUserTypes() {
        log.debug("Fetching all UserType");

        return userTypeRepo.findAll()
            .stream()
            .map(userTypeMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a user type by ID
     * 
     * @param id UserType ID
     * @return UserTypeResponseDTO
     * @throws EntityNotFoundException if user type not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserTypeResponseDTO getUserTypeById(Long id) {
        log.debug("Fetching UserType with ID: {}", id);

        UserType userType = findUserTypeByIdOrThrow(id);

        return userTypeMapper.toResponseDTO(userType);
    }

    /**
     * Retrieves a user type by NAME
     * 
     * @param name UserType NAME
     * @return UserTypeResponseDTO
     * @throws EntityNotFoundException if user type not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserTypeResponseDTO getUserTypeByName(String name) {
        log.debug("Fetching UserType with NAME: {}", name);

        UserType userType = findUserTypeByNameOrThrow(name);

        return userTypeMapper.toResponseDTO(userType);
    }

    /**
     * Creates a new user type
     * 
     * @param dto UserTypeCreateDTO
     * @return UserTypeResponseDTO
     * @throws DataIntegrityViolationException if user type already exists
     * @throws RuntimeException if failed to create user type
     */
    @Override
    @Transactional
    public UserTypeResponseDTO createUserType(UserTypeCreateDTO dto) {
        log.info("Creating UserType: {}", dto);

        validateUserTypeNameNotExists(dto.getName());

        try {
            UserType userType = userTypeMapper.fromCreateDTO(dto);
            userType = userTypeRepo.save(userType);
            log.info("Successfully created UserType with ID: {}", userType.getId());
            return userTypeMapper.toResponseDTO(userType);
        } catch (Exception e) {
            log.error("Error creating UserType: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create UserType");
        }
    }

    /**
     * Updates an user type
     * 
     * @param id UserType ID
     * @param dto UserTypeCreateDTO
     * @return UserTypeResponseDTO
     * @throws EntityNotFoundException if user type not found
     * @throws DataIntegrityViolationException if user type already exists
     * @throws RuntimeException if failed to update user type
     */
    @Override
    @Transactional
    public UserTypeResponseDTO updateUserType(Long id, UserTypeCreateDTO dto) {
        log.info("Updating UserType with ID: {}", id);

        UserType userType = findUserTypeByIdOrThrow(id);
        
        if (utils.hasNameChanged(userType.getName(), dto.getName())) {
            validateUserTypeNameNotExists(dto.getName());
        }

        try {
            userTypeMapper.updateFromDTO(dto, userType);
            UserType updated = userTypeRepo.save(userType);
            log.info("Successfully updated UserType with ID: {}", id);
            return userTypeMapper.toResponseDTO(updated);
        } catch (Exception e) {
            log.error("Error updating UserType {}", e.getMessage(), e);
            throw new RuntimeException("Error updating UserType");
        }
    }

    /**
     * Deletes a user type by ID
     * @param id UserType ID to delete
     * @throws EntityNotFoundException if user type not found
     * @throws RuntimeException if failed to delete user type
     */
    @Override
    @Transactional
    public void deleteUserType(Long id) {
        log.info("Deleting UserType with ID: {}", id);

        findUserTypeByIdOrThrow(id);

        try {
            userTypeRepo.deleteById(id);
            log.info("Successfully deleted UserType with ID: {}", id); 
        } catch (Exception e) {
            log.error("Error deleting UserType with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete UserType", e);
        }
    }

    /**
     * Method to find a user type by ID
     * 
     * @param id UserType ID
     * @return UserType
     * @throws EntityNotFoundException if user type not found
     */
    public UserType findUserTypeByIdOrThrow(Long id) {
        return userTypeRepo.findById(id)
            .orElseThrow(() -> {
                log.error("UserType with ID: {} not found", id);
                throw new EntityNotFoundException("UserType not found");
            });
    }

    /**
     * Method to find a user type by NAME
     * 
     * @param name UserType NAME
     * @return UserType
     * @throws EntityNotFoundException if user type not found
     */
    public UserType findUserTypeByNameOrThrow(String name) {
        return userTypeRepo.findByName(name.trim())
            .orElseThrow(() -> {
                log.error("UserType with NAME: {} not found", name);
                throw new EntityNotFoundException("UserType not found");
            });
    }

    /**
     * Method to validate a user type does not exist by NAME
     * 
     * @param name UserType NAME
     * @throws DataIntegrityViolationException if user type already exists
     */
    public void validateUserTypeNameNotExists(String name) {
        if (userTypeRepo.findByName(name.trim()).isPresent()) {
            log.warn("Cannot create UserType - NAME '{}' already exists", name);
            throw new DataIntegrityViolationException("UserType with this name already exists");
        }
    }
}