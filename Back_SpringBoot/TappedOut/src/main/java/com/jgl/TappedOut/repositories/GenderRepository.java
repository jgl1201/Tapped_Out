package com.jgl.TappedOut.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.Gender;

/**
 * Repository interface for managing {@link Gender} entities
 * Provides basic CRUD operations
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {
    Optional<Gender> findByName(String name);
}