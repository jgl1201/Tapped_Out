package com.jgl.TappedOut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.Sport;

/**
 * Repository interface for managing {@link Sport} entities
 * Provides basic CRUD operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
    boolean existsByName(String name);

    Sport findByName(String name);
}
