package com.jgl.TappedOut.service;

import java.math.BigDecimal;
import java.util.List;

import com.jgl.TappedOut.dto.CategoryCreateDTO;
import com.jgl.TappedOut.dto.CategoryResponseDTO;
import com.jgl.TappedOut.dto.CategoryUpdateDTO;

/**
 * Interface to declare methods needed at {@link CategoryServiceImpl}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();
    List<CategoryResponseDTO> getCategoriesBySportId(Long sportId);
    List<CategoryResponseDTO> getCategoriesByGenderId(Long genderId);
    List<CategoryResponseDTO> getCategoriesByLevelId(Long levelId);
    List<CategoryResponseDTO> searchCategories(Long sportId, Long genderId, Long levelId, Integer minAge, Integer maxAge, BigDecimal minWeight, BigDecimal maxWeight);
    CategoryResponseDTO getCategoryById(Long id);
    CategoryResponseDTO getCategoryBySportAndName(Long sportId, String name);
    CategoryResponseDTO createCategory(CategoryCreateDTO dto);
    CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO dto);
    void deleteCategory(Long id);
}