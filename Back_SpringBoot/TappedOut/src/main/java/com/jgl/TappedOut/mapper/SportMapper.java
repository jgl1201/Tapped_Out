package com.jgl.TappedOut.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.jgl.TappedOut.dto.SportCreateDTO;
import com.jgl.TappedOut.dto.SportResponseDTO;
import com.jgl.TappedOut.models.Sport;

/**
 * Mapper for converting between {@link Result} entities and DTOs.
 * Handles mapping including validations after it.
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SportMapper {
    /**
     * Method to convert {@link SportCreateDTO} into a {@link Sport}
     * 
     * @param dto the SportCreateDTO to convert from
     * @return the mapped Gender entity to persist
     */
    public abstract Sport fromCreateDTO(SportCreateDTO dto);

    /**
     * Method to convert {@link Sport} into a {@link SportResponseDTO}
     * 
     * @param sport the Sport to show
     * @return the mapped response DTO
     */
    public abstract SportResponseDTO toResponseDTO(Sport sport);

    /**
     * Method to convert {@link SportUpdateDTO} into a {@link Sport}
     * 
     * @param dto the SportUpdateDTO to update from
     * @param sport the Sport entity to be updated
     */
    public abstract void updateFromDTO(SportCreateDTO dto, @MappingTarget Sport sport);
}