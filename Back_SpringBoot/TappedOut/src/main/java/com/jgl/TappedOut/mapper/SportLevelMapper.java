package com.jgl.TappedOut.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.jgl.TappedOut.dto.SportLevelCreateDTO;
import com.jgl.TappedOut.dto.SportLevelResponseDTO;
import com.jgl.TappedOut.dto.SportLevelUpdateDTO;
import com.jgl.TappedOut.models.Result;
import com.jgl.TappedOut.models.SportLevel;

/**
 * Mapper for converting between {@link Result} entities and DTOs.
 * Handles mapping including validations after it.
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Mapper(componentModel = "spring",
    uses = {SportMapper.class, MapperUtils.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SportLevelMapper {
    @Autowired
    protected MapperUtils mapperUtils;

    /**
     * Method to convert {@link SportLevelCreateDTO} into a {@link SportLevel}
     * 
     * @param dto the SportLevelCreateDTO to convert from
     * @return the mapped SportLevel entity to persist
     * @throws IllegalArgumentException if name already exists
     */
    @Mapping(target = "sportId", expression = "java(mapperUtils.mapSport(dto.getSportId()))")
    @Mapping(target = "id", ignore = true)
    public abstract SportLevel fromCreateDTO(SportLevelCreateDTO dto);

    /**
     * Method to convert {@link SportLevel} into a {@link SportLevelResponseDTO}
     * 
     * @param sportLevel the SportLevel to show
     * @return the mapped response DTO
     */
    @Mapping(target = "sport", source = "sportId")
    public abstract SportLevelResponseDTO toResponseDTO(SportLevel sportLevel);

    /**
     * Method to convert {@link SportLevelUpdateDTO} into a {@link SportLevel}
     * 
     * @param dto the SportLevelUpdateDTO to update from
     * @param sportLevel the SportLevel entity to be updated
     * @throws IllegalArgumentException if name already exists
     */
    @Mapping(target = "sportId", expression = "java(mapperUtils.mapSport(dto.getSportId()))")
    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDTO(SportLevelUpdateDTO dto, @MappingTarget SportLevel sportLevel);

    /**
     * Validates SportLevel after mapping
     * 
     * @param sportLevel the model {@link SportLevel}
     * @throws IllegalArgumentException if name already exists
     */
    @AfterMapping
    protected void validateSportLevel(@MappingTarget SportLevel sportLevel) {
        mapperUtils.validateUniqueName(sportLevel.getName());
    }

    /**
     * Validates SportLevel update after mapping
     * If name changes, validate it's unique, else do nothing
     * 
     * @param dto the SportLevelUpdateDTO that's been mapped to flag when to validate (updateFromDTO)
     * @param sportLevel the model {@link SportLevel}
     * @throws IllegalArgumentException if name already exists
     */
    @AfterMapping
    protected void validateSportLevelUpdate(SportLevelUpdateDTO dto, @MappingTarget SportLevel sportLevel) {
        if (!dto.getName().equalsIgnoreCase(sportLevel.getName()))
            mapperUtils.validateUniqueName(dto.getName());
    }
}