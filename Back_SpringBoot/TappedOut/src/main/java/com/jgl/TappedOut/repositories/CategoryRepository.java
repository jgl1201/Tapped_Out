package com.jgl.TappedOut.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.Category;
import com.jgl.TappedOut.models.Gender;
import com.jgl.TappedOut.models.Sport;
import com.jgl.TappedOut.models.SportLevel;

/**
 * Repository interface for managing {@link Category} entities.
 * Provides methods to query categories based on different criteria including
 * sport, gender, level, age range and weight range
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findBySportId(Sport sportId);

    List<Category> findByGenderId(Gender genderId);

    List<Category> findByLevelId(SportLevel levelId);

    Optional<Category> findBySportIdAndName(Sport sportId, String name);

    /**
     * Finds categories matching the specified filters.
     * 
     * @param sport mandatory sport filter
     * @param gender mandatory gender filter
     * @param level optional level filter
     * @param minAge optional minAge filter (categories with min_age <= this value)
     * @param maxAge optional maxAge filter (categories with max_age >= this value)
     * @param minWeight optional minWeight filter (categories with min_weight <= this value)
     * @param maxWeight optional maxWeight filter (categories with max_weight >= this value)
     * 
     * @return list of matching categories
     */
    @Query("SELECT c FROM Category c WHERE " +
        "c.sportId.id = :sport AND " +
        "c.genderId.id = :gender AND " +
        "(:level IS NULL OR c.levelId.id = :level) AND " +
        "(:minAge IS NULL OR c.minAge IS NULL OR c.minAge <= :minAge) AND " +
        "(:maxAge IS NULL OR c.maxAge IS NULL OR c.maxAge >= :maxAge) AND " +
        "(:minWeight IS NULL OR c.minWeight IS NULL OR c.minWeight <= :minWeight) AND " +
        "(:maxWeight IS NULL OR c.maxWeight IS NULL OR c.maxWeight >= :maxWeight)")
    List<Category> findMatchingCategories(
        @Param("sport") Sport sport,
        @Param("gender") Gender gender,
        @Param("level") SportLevel level,
        @Param("minAge") Integer minAge,
        @Param("maxAge") Integer maxAge,
        @Param("minWeight") BigDecimal minWeight,
        @Param("maxWeight") BigDecimal maxWeight
    );
}