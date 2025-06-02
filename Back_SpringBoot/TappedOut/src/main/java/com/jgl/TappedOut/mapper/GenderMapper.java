package com.jgl.TappedOut.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.jgl.TappedOut.dto.GenderCreateDTO;
import com.jgl.TappedOut.dto.GenderResponseDTO;
import com.jgl.TappedOut.models.Gender;

/**
 * Mapper for converting between {@link Gender} entities and DTOs.
 * Handles mapping including validations after it.
 * 
 * @Mapping annotations can be omited due to coincidence in variables' names
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class GenderMapper {
    /**
     * Method to convert {@link GenderCreateDTO} into a {@link Gender}
     * 
     * @param dto the GenderCreateDTO to convert
     * @return the mapped Gender entity
     */
    public abstract Gender fromCreateDTO(GenderCreateDTO dto);

    /**
     * Method to convert {@link Gender} into a {@link GenderResponseDTO}
     * 
     * @param gender the Gender to show
     * @return the mapped response DTO
     */
    public abstract GenderResponseDTO toResponseDTO(Gender gender);

    /**
     * Method to convert {@link GenderUpdateDTO} into a {@link Gender}
     * 
     * @param dto the GenderUpdateDTO
     * @param entity the entity to update
     */
    public abstract void updateFromDTO(GenderCreateDTO dto, @MappingTarget Gender entity);
}