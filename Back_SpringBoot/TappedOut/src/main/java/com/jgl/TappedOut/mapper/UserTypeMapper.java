package com.jgl.TappedOut.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.jgl.TappedOut.dto.UserTypeCreateDTO;
import com.jgl.TappedOut.dto.UserTypeResponseDTO;
import com.jgl.TappedOut.models.UserType;

/**
 * Mapper for converting between {@link UserType} entities and DTOs.
 * Handles mapping including validations after it.
 * 
 * @author Jorge García López
 * @version 1.1
 * @since 2025
 */
@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserTypeMapper {
    /**
     * Method to convert {@link UserTypeCreateDTO} into a {@link UserType}
     * 
     * @param dto the UserTypeCreateDTO to convert from
     * @return the mapped UserType entity to persist
     */
    @Mapping(target = "id", ignore = true)
    public abstract UserType fromCreateDTO(UserTypeCreateDTO dto);
    
    /**
     * Method to convert {@link UserType} into a {@link UserTypeResponseDTO}
     * 
     * @param userType the UserType to show
     * @return the mapped response DTO
     */
    public abstract UserTypeResponseDTO toResponseDTO(UserType user);
    
    /**
     * Method to convert {@link UserTypeCreateDTO} into a {@link UserType}
     * 
     * @param dto the UserTypeCreateDTO to update from
     * @param userType the entity UserType to be updated
     */
    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDTO(UserTypeCreateDTO dto, @MappingTarget UserType userType);
}