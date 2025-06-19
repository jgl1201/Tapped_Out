package com.jgl.TappedOut.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import java.math.BigDecimal;

import com.jgl.TappedOut.service.CategoryService;
import com.jgl.TappedOut.dto.CategoryResponseDTO;
import com.jgl.TappedOut.dto.CategoryCreateDTO;
import com.jgl.TappedOut.dto.CategoryUpdateDTO;

/**
 * Controller to define endpoints for Category
 * 
 * @author Jorge García Lopez
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/category")
@Tag(name = "Category", description = "API Endpoints for Category management")
public class CategoryRestController extends BaseController {
    @Autowired
    private CategoryService categoryService;

    /**
     * GET /api/tappedout/category
     * Retrieves all categories
     * 
     * @return List of CategoryResponseDTO
     */
    @GetMapping({"", "/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves all categories",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of categories",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/tappedout/category/sport/{sportId}
     * Retrieves categories by sport ID
     * 
     * @param sportId Sport ID
     * @return List of CategoryResponseDTO
     * @throws EntityNotFoundException if sport not found
     */
    @GetMapping({"/sport/{sportId}", "/sport/{sportId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves categories by sport ID",
        parameters = {
            @Parameter(name = "sportId", description = "Sport ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of categories for the sport",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesBySportId(@PathVariable("sportId") Long sportId) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesBySportId(sportId);
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/tappedout/category/gender/{genderId}
     * Retrieves categories by gender ID
     * 
     * @param genderId Gender ID
     * @return List of CategoryResponseDTO
     * @throws EntityNotFoundException if gender not found
     */
    @GetMapping({"/gender/{genderId}", "/gender/{genderId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves categories by gender ID",
        parameters = {
            @Parameter(name = "genderId", description = "Gender ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of categories for the gender",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Gender not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByGenderId(@PathVariable("genderId") Long genderId) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesByGenderId(genderId);
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/tappedout/category/level/{levelId}
     * Retrieves categories by sport level ID
     * 
     * @param levelId Sport Level ID
     * @return List of CategoryResponseDTO
     * @throws EntityNotFoundException if sport level not found
     */
    @GetMapping({"/level/{levelId}", "/level/{levelId}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves categories by sport level ID",
        parameters = {
            @Parameter(name = "levelId", description = "Sport Level ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found list of categories for the sport level",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport level not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByLevelId(@PathVariable("levelId") Long levelId) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesByLevelId(levelId);
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/tappedout/category/search
     * Searches categories by multiple filters
     * 
     * @param sportId Sport ID (required)
     * @param genderId Gender ID (required)
     * @param levelId Sport Level ID (optional)
     * @param minAge Minimum age (optional)
     * @param maxAge Maximum age (optional)
     * @param minWeight Minimum weight (optional)
     * @param maxWeight Maximum weight (optional)
     * @return List of CategoryResponseDTO
     * @throws EntityNotFoundException if sport, gender or level not found
     */
    @GetMapping({"/search", "/search/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Searches categories by multiple filters",
        parameters = {
            @Parameter(name = "sportId", description = "Sport ID", example = "1", required = true),
            @Parameter(name = "genderId", description = "Gender ID", example = "1", required = true),
            @Parameter(name = "levelId", description = "Sport Level ID", example = "1", required = false),
            @Parameter(name = "minAge", description = "Minimum age", example = "18", required = false),
            @Parameter(name = "maxAge", description = "Maximum age", example = "30", required = false),
            @Parameter(name = "minWeight", description = "Minimum weight in kg", example = "70.5", required = false),
            @Parameter(name = "maxWeight", description = "Maximum weight in kg", example = "80.0", required = false)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found matching categories",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport, gender or level not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<List<CategoryResponseDTO>> searchCategories(@RequestParam("sportId") Long sportId, @RequestParam("genderId") Long genderId,  @RequestParam(value = "levelId", required = false) Long levelId, @RequestParam(value = "minAge", required = false) Integer minAge, @RequestParam(value = "maxAge", required = false) Integer maxAge, @RequestParam(value = "minWeight", required = false) BigDecimal minWeight, @RequestParam(value = "maxWeight", required = false) BigDecimal maxWeight) {
        List<CategoryResponseDTO> categories = categoryService.searchCategories(
            sportId, genderId, levelId, minAge, maxAge, minWeight, maxWeight);
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/tappedout/category/{id}
     * Retrieves a category by ID
     * 
     * @param id Category ID
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if category not found
     */
    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a category by ID",
        parameters = {
            @Parameter(name = "id", description = "Category ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found category",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Category not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable("id") Long id) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * GET /api/tappedout/category/sport/{sportId}/name/{name}
     * Retrieves a category by sport ID and name
     * 
     * @param sportId Sport ID
     * @param name Category name
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if category or sport not found
     */
    @GetMapping({"/sport/{sportId}/name/{name}", "/sport/{sportId}/name/{name}/"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'COMPETITOR')")
    @Operation(
        summary = "Retrieves a category by sport ID and name",
        parameters = {
            @Parameter(name = "sportId", description = "Sport ID", example = "1", required = true),
            @Parameter(name = "name", description = "Category name", example = "ADULT_MALE_BEGINNER", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found category",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Category or sport not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<CategoryResponseDTO> getCategoryBySportAndName(@PathVariable("sportId") Long sportId, @PathVariable("name") String name) {
        CategoryResponseDTO category = categoryService.getCategoryBySportAndName(sportId, name);
        return ResponseEntity.ok(category);
    }

    /**
     * POST /api/tappedout/category
     * Creates a new category
     * 
     * @param dto CategoryCreateDTO
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if sport, gender or level not found
     * @throws DataIntegrityViolationException if category name already exists
     * @throws RuntimeException if failed to create category
     */
    @PostMapping({"", "/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Creates a new category",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Category created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Sport, gender or level not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Category with this name already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryCreateDTO dto) {
        CategoryResponseDTO created = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/tappedout/category/{id}
     * Updates a category
     * 
     * @param id Category ID
     * @param dto CategoryUpdateDTO
     * @return CategoryResponseDTO
     * @throws EntityNotFoundException if category, sport, gender or level not found
     * @throws DataIntegrityViolationException if category name already exists
     * @throws RuntimeException if failed to update category
     */
    @PutMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Updates a category",
        parameters = {
            @Parameter(name = "id", description = "Category ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Category updated successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Category, sport, gender or level not found",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Category with this name already exists",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDTO.class))
            )
        }
    )
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryUpdateDTO dto) {
        CategoryResponseDTO updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/tappedout/category/{id}
     * Deletes a category
     * 
     * @param id Category ID
     * @throws EntityNotFoundException if category not found
     * @throws RuntimeException if failed to delete category
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Deletes a category",
        parameters = {
            @Parameter(name = "id", description = "Category ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Category deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Category not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}