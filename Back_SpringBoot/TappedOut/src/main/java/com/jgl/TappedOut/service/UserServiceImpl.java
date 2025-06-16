package com.jgl.TappedOut.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgl.TappedOut.dto.UserCreateDTO;
import com.jgl.TappedOut.dto.UserResponseDTO;
import com.jgl.TappedOut.dto.UserSecurityDTO;
import com.jgl.TappedOut.dto.UserUpdateDTO;
import com.jgl.TappedOut.mapper.UserMapper;
import com.jgl.TappedOut.models.Gender;
import com.jgl.TappedOut.models.User;
import com.jgl.TappedOut.models.UserType;
import com.jgl.TappedOut.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service class to handle logic related with {@link User}
 * 
 * ? Interacts with the UserRepository in order to perform basic
 * ? CRUD operations and other specific domain operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private CustomLogger log;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTypeServiceImpl userTypeService;

    @Autowired
    private GenderServiceImpl genderService;


    /**
     * Retrieves all users
     * 
     * @return List of UserResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        log.debug("Fetching all User");

        return userRepo.findAll().stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves users by type
     * 
     * @param typeId UserType ID
     * @return List of UserResponseDTO
     * @throws EntityNotFoundException if user type not found from UserTypeService
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByType(Long typeId) {
        log.debug("Fetching User\n\tUserType ID: {}", typeId);

        UserType userType = userTypeService.findUserTypeByIdOrThrow(typeId);

        return userRepo.findByTypeId(userType)
            .orElseThrow(() -> new EntityNotFoundException("User not found for user type ID " + typeId))
            .stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves users by gender
     * 
     * @param genderId Gender ID
     * @return List of UserResponseDTO
     * @throws EntityNotFoundException if gender not found from GenderService
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByGender(Long genderId) {
        log.debug("Fetching User\n\tGender ID: {}", genderId);

        Gender gender = genderService.findGenderByIdOrThrow(genderId);
        
        return userRepo.findByGenderId(gender)
            .orElseThrow(() -> new EntityNotFoundException("User not found for gender ID " + genderId))
            .stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves users by location
     * 
     * @param country Country
     * @param city City
     * @return List of UserResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByLocation(String country, String city) {
        log.debug("Fetching User\n\tLocation -\n\t\tCountry: {}, City: {}", country, city);

        return userRepo.findByCountryAndCity(country.trim(), city.trim())
            .orElseThrow(() -> new EntityNotFoundException("User not found for location " + country + ", " + city))
            .stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Searches users by name or email
     * 
     * @param query Search term
     * @return List of UserResponseDTO matching the query
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsers(String query) {
        log.debug("Searching User\n\tQuery: {}", query);

        return userRepo.searchUsers(query.trim()).stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by ID
     * 
     * @param id User ID
     * @return UserResponseDTO
     * @throws EntityNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        log.debug("Fetching User with ID: {}", id);

        User user = findUserByIdOrThrow(id);

        return userMapper.toResponseDTO(user);
    }

    /**
     * Retrieves a user by DNI
     * 
     * @param dni User DNI
     * @return UserResponseDTO
     * @throws EntityNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByDni(String dni) {
        String normalizedDni = dni.trim().toUpperCase();
        log.debug("Fetching user with DNI: {}", normalizedDni);

        User user = findUserByDniOrThrow(normalizedDni);

        return userMapper.toResponseDTO(user);
    }

    /**
     * Retrieves a user by email
     * 
     * @param email User email
     * @return UserResponseDTO
     * @throws EntityNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        String normalizedEmail = email.trim().toLowerCase();
        log.debug("Fetching user with EMAIL: {}", normalizedEmail);

        User user = findUserByEmailOrThrow(normalizedEmail);

        return userMapper.toResponseDTO(user);
    }

    /**
     * Creates a new user
     * 
     * @param dto UserCreateDTO
     * @return UserResponseDTO
     * @throws DataIntegrityViolationException if DNI or email already exist
     * @throws RuntimeException if user creation fails
     */
    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO dto) {
        String normalizedDni = dto.getDni().trim().toUpperCase();
        String normalizedEmail = dto.getEmail().trim().toLowerCase();
        log.info("Creating new user with DNI: {} and EMAIL: {}", normalizedDni, normalizedEmail);

        validateUserDniNotExists(normalizedDni);
        validateUserEmailNotExists(normalizedEmail);

        try {
            User user = userMapper.fromCreateDTO(dto);
            user = userRepo.save(user);
            log.info("Successfully created new User with ID: {}", user.getId());
            return userMapper.toResponseDTO(user);
        } catch (Exception e) {
            log.error("Error creating User: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create User");
        }
    }

    /**
     * Updates user basic information
     * 
     * @param id User ID
     * @param dto UserUpdateDTO
     * @return Updated UserResponseDTO
     * @throws EntityNotFoundException if user not found
     * @throws RuntimeException if user update fails
     */
    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        log.info("Updating user with ID: {}", id);

        User user = findUserByIdOrThrow(id);

        try {
            userMapper.updateFromDTO(dto, user);
            User updatedUser = userRepo.save(user);
            log.info("Successfully updated User with ID: {}", id);
            return userMapper.toResponseDTO(updatedUser);
        } catch (Exception e) {
            log.error("Error updating User with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update User");
        }
    }

    /**
     * Updates user auth information (email and password)
     * 
     * @param id User ID
     * @param dto UserSecurityDTO
     * @return UserResponseDTO
     * @throws EntityNotFoundException if user not found
     * @throws RuntimeException if user security update fails
     */
    @Override
    @Transactional
    public UserResponseDTO updateUserSecurity(Long id, UserSecurityDTO dto) {
        String newEmail = dto.getEmail().trim().toLowerCase();
        log.info("Updating authentication info for user with ID: {}", id);

        User user = findUserByIdOrThrow(id);

        if (!user.getEmail().equalsIgnoreCase(newEmail))
            validateUserEmailNotExists(newEmail);

        try {
            userMapper.updateSecurity(dto, user);
            User updatedUser = userRepo.save(user);
            log.info("Successfully updated authentication info for User with ID: {}", id);
            return userMapper.toResponseDTO(updatedUser);
        } catch (Exception e) {
            log.error("Error updating authentication info for User with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update User authentication");
        }
    }

    /**
     * Deletes a user by ID
     * 
     * @param id User ID
     * @throws EntityNotFoundException if user not found
     * @throws RuntimeException if failed to delete user
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        findUserByIdOrThrow(id);

        try {
            userRepo.deleteById(id);
            log.info("Successfully deleted User with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting User with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete User");
        }
    }

    /**
     * Method to find a user by ID
     * 
     * @param id User ID
     * @return User
     * @throws EntityNotFoundException if user not found
     */
    public User findUserByIdOrThrow(Long id) {
        return userRepo.findById(id)
            .orElseThrow(() -> {
                log.error("User with ID: {} not found", id);
                throw new EntityNotFoundException("User not found");
            });
    }

    /**
     * Method to find a user by DNI
     * 
     * @param dni User DNI
     * @return User
     * @throws EntityNotFoundException if user not found
     */
    public User findUserByDniOrThrow(String dni) {
        return userRepo.findByDni(dni.trim().toUpperCase())
            .orElseThrow(() -> {
                log.error("User with DNI: {} not found", dni);
                throw new EntityNotFoundException("User not found");
            });
    }

    /**
     * Method to find a user by EMAIL
     * 
     * @param email User EMAIL
     * @return User
     * @throws EntityNotFoundException if user not found
     */
    public User findUserByEmailOrThrow(String email) {
        return userRepo.findByEmail(email.trim().toLowerCase())
            .orElseThrow(() -> {
                log.error("User with EMAIL: {} not found", email);
                throw new EntityNotFoundException("User not found");
            });
    }

    /**
     * Method to validate user does not exist by DNI
     * 
     * @param dni User DNI
     * @throws DataIntegrityViolationException if user already exists
     */
    public void validateUserDniNotExists(String dni) {
        if (userRepo.findByDni(dni.trim().toUpperCase()).isPresent()) {
            log.warn("Cannot create User - DNI '{}' already exists", dni);
            throw new DataIntegrityViolationException("User with this DNI already exists");
        }
    }

    /**
     * Method to validate user does not exist by EMAIL
     * 
     * @param email User EMAIL
     * @throws DataIntegrityViolationException if user already exists
     */
    public void validateUserEmailNotExists(String email) {
        if (userRepo.findByEmail(email.trim().toLowerCase()).isPresent()) {
            log.warn("Cannot create User - EMAIL '{}' already exists", email);
            throw new DataIntegrityViolationException("User with this email already exists");
        }
    }
}