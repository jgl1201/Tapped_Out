package com.jgl.TappedOut.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.UserType;

/**
 * Repository interface for managing {@link UserType} entities
 * Provides basic CRUD operations
 * 
 * @author jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long>{
    Optional<UserType> findByName(String name);
}