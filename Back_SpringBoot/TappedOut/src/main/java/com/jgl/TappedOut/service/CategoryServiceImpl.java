package com.jgl.TappedOut.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jgl.TappedOut.dto.CategoryCreateDTO;
import com.jgl.TappedOut.dto.CategoryResponseDTO;
import com.jgl.TappedOut.dto.CategoryUpdateDTO;
import com.jgl.TappedOut.mapper.CategoryMapper;
import com.jgl.TappedOut.models.Category;
import com.jgl.TappedOut.models.Gender;
import com.jgl.TappedOut.models.Sport;
import com.jgl.TappedOut.models.SportLevel;
import com.jgl.TappedOut.repositories.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service class to handle logic related with {@link Category}
 * 
 * ? Interacts with the CategoryRepository and other services to perform
 * ? CRUD operations and other specific domain operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CustomLogger log;

    @Autowired
    private Utils utils;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SportServiceImpl sportServiceImpl;

    @Autowired
    private GenderServiceImpl genderServiceImpl;

    @Autowired
    private SportLevelServiceImpl sportLevelServiceImpl;


    /** Retrieves all categories
     * 
     * @return List of CategoryResponseDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        log.debug("Fetching all Category");

        return categoryRepo.findAll().stream()
            .map(categoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a category by Sport ID
     * 
     * @param sportId Sport ID
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if sport not found from SportService
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getCategoriesBySportId(Long sportId) {
        log.debug("Fetching Category\n\tSport ID: {}", sportId);

        Sport sport = sportServiceImpl.findSportByIdOrThrow(sportId);

        return categoryRepo.findBySportId(sport)
            .orElseThrow(() -> new EntityNotFoundException("Category not found for sport ID " + sportId))
            .stream()
            .map(categoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a category by Gender ID
     * 
     * @param genderId Gender ID
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if gender not found from GenderService
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getCategoriesByGenderId(Long genderId) {
        log.debug("Fetching Category\n\tGender ID: {}", genderId);

        Gender gender = genderServiceImpl.findGenderByIdOrThrow(genderId);

        return categoryRepo.findByGenderId(gender)
            .orElseThrow(() -> new EntityNotFoundException("Category not found for gender ID " + genderId))
            .stream()
            .map(categoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a category by Sport Level ID
     * 
     * @param levelId Sport Level ID
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if sport level not found from SportLevelService
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getCategoriesByLevelId(Long levelId) {
        log.debug("Fetching Category\n\tSportLevel ID: {}", levelId);

        SportLevel sportLevel = sportLevelServiceImpl.findSportLevelByIdOrThrow(levelId);

        return categoryRepo.findByLevelId(sportLevel)
            .orElseThrow(() -> new EntityNotFoundException("Category not found for level ID " + levelId))
            .stream()
            .map(categoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Retrieves categories by filter
     * 
     * @param sportId Sport ID
     * @param genderId Gender ID
     * @param levelId Sport Level ID
     * @param minAge Minimum age
     * @param maxAge Maximum age
     * @param minWeight Minimum weight
     * @param maxWeight Maximum weight
     * @return List of CategoryResponseDTO
     * @throws EntityNotFoundException if referenced sport, gender or level not found from their services
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> searchCategories(Long sportId, Long genderId, Long levelId, Integer minAge, Integer maxAge, BigDecimal minWeight, BigDecimal maxWeight) {
        
        log.debug("Searching Category\n\tSport: {},\n\tGender: {},\n\tLevel: {},\n\tMinAge: {}, MaxAge: {},\n\tMinWeight: {}, MaxWeight: {}",
            sportId, genderId, levelId, minAge, maxAge, minWeight, maxWeight);

        Sport sport = sportServiceImpl.findSportByIdOrThrow(sportId);
        Gender gender = genderServiceImpl.findGenderByIdOrThrow(genderId);
        SportLevel level = levelId != null ? sportLevelServiceImpl.findSportLevelByIdOrThrow(levelId) : null;

        List<Category> categories = categoryRepo.findMatchingCategories(
            sport, gender, level, minAge, maxAge, minWeight, maxWeight
        );

        return categories.stream()
            .map(categoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a category by ID
     * 
     * @param id Category ID
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if category not found
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        log.debug("Fetching Category with ID: {}", id);

        Category category = findCategoryByIdOrThrow(id);

        return categoryMapper.toResponseDTO(category);
    }

    /**
     * Retrieves a category by Sport and Name
     * 
     * @param sportId Sport ID
     * @param name Category name
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if category not found
     * @throws EntityNotFoundException if sport not found from SportService
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryBySportAndName(Long sportId, String name) {
        log.debug("Fetching Category\n\tSport ID: {}\n\tName: {}", sportId, name);

        Sport sport = sportServiceImpl.findSportByIdOrThrow(sportId);
        Category category = findCategoryBySportAndNameOrThrow(sport, name);

        return categoryMapper.toResponseDTO(category);
    }

    /**
     * Creates a new category
     * 
     * @param dto CategoryCreateDTO
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if sport not found from SportService
     * @throws EntityNotFoundException if gender not found from GenderService
     * @throws EntityNotFoundException if level not found from SportLevelService
     * @throws DataIntegrityViolationException if category with the same name already exists
     * @throws RuntimeException if failed to create category
     */
    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryCreateDTO dto) {
        log.debug("Creating Category: {}", dto);

        try {
            Category category = categoryMapper.fromCreateDTO(dto);
            validateCategoryNotExists(category);
            category = categoryRepo.save(category);
            log.info("Successfully created Category ID: {}", category.getId());
            return categoryMapper.toResponseDTO(category);
        } catch(Exception e) {
            log.error("Error creating Category: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create Category", e);
        }
    }

    /**
     * Updates a category
     * 
     * @param id Category ID
     * @param dto CategoryUpdateDTO
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if category not found
     * @throws EntityNotFoundException if sport not found from SportService
     * @throws EntityNotFoundException if gender not found from GenderService
     * @throws EntityNotFoundException if level not found from SportLevelService
     * @throws DataIntegrityViolationException if category with the same name already exists
     * @throws RuntimeException if failed to update category
     */
    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO dto) {
        log.debug("Updating Category ID: {}", id);

        Category category = findCategoryByIdOrThrow(id);

        if (utils.hasNameChanged(category.getName(), dto.getName())) {
            validateCategoryNotExists(category);
        }

        try {
            categoryMapper.updateFromDTO(dto, category);
            category = categoryRepo.save(category);
            log.info("Successfully updated Category ID: {}", category.getId());
            return categoryMapper.toResponseDTO(category);
        } catch(Exception e) {
            log.error("Error updating Category with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update Category");
        }
    }

    /**
     * Deletes a category
     * 
     * @param id Category ID
     * @throws EntityNotFoundException if category not found
     * @throws RuntimeException if failed to delete category
     */
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting Category with ID: {}", id);

        findCategoryByIdOrThrow(id);

        try {
            categoryRepo.deleteById(id);
            log.info("Successfully deleted Category with ID: {}", id);
        } catch(Exception e) {
            log.error("Error deleting Category with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete Category");
        }
    }

    /**
     * Method to find a category by ID
     * 
     * @param id Category ID
     * @return Category
     * @throws EntityNotFoundException if category not found
     */
    public Category findCategoryByIdOrThrow(Long id) {
        return categoryRepo.findById(id)
            .orElseThrow(() -> {
                log.error("Category ID: {} not found", id);
                throw new EntityNotFoundException("Category not found");
            });
    }

    /**
     * Method to find a category by Sport and Name
     * 
     * @param sport Sport
     * @param name Category name
     * @return Category
     * @throws EntityNotFoundException if category not found
     * @throws RuntimeException if failed to find category
     */
    public Category findCategoryBySportAndNameOrThrow(Sport sport, String name) {
        return categoryRepo.findBySportIdAndName(sport, name.trim())
            .orElseThrow(() -> {
                log.error("Category not found for Sport ID: {} and Name: {}", sport.getId(), name);
                throw new EntityNotFoundException("Category not found for sport ID " + sport.getId() + " and name " + name);
            });
    }

    /**
     * Method to validate category does not exist
     * 
     * @param category Category
     * @throws DataIntegrityViolationException if category already exists
     */
    public void validateCategoryNotExists(Category category) {
        if (categoryRepo.findBySportIdAndName(category.getSportId(), category.getName()).isPresent()) {
            log.warn("Category with NAME: '{}' already exists for Sport: {}", category.getName(), category.getSportId().getId());
            throw new DataIntegrityViolationException("Category with this name already exists for the selected sport");
        }
    }
}