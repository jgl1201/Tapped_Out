package com.jgl.TappedOut.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.Sport;
import com.jgl.TappedOut.models.SportLevel;

/**
 * Repository interface for managign {@link SportLevel} entities
 * Provides methods to query sport skill levels and their relationships
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface SportLevelRepository extends JpaRepository<SportLevel, Long> {
    Optional<List<SportLevel>> findBySportId(Sport sportId);

    Optional<SportLevel> findBySportIdAndName(Sport sportId, String name);
}