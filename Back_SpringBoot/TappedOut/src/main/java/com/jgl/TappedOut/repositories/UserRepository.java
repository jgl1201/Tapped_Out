package com.jgl.TappedOut.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jgl.TappedOut.models.Gender;
import com.jgl.TappedOut.models.User;
import com.jgl.TappedOut.models.UserType;

/**
 * Reporitory interface for managing {@link User} entities
 * Provides methods for user authentication, search and profile management
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByTypeId(UserType typeId);

    List<User> findByGenderId(Gender genderId);

    List<User> findByCountryAndCity(String country, String city);
    
    Optional<User> findByDni(String dni);

    Optional<User> findByEmail(String email);

    boolean existsByDniIgnoreCase(String dni);

    boolean existsByEmailIgnoreCase(String email);

    /**
     * Finds users by name or email
     * 
     * @param query the search term
     * 
     * @return list of matching results
     */
    @Query("SELECT u FROM User u WHERE " +
        "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<User> searchUsers(@Param("query") String query);
}