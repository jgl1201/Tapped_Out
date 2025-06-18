package com.jgl.TappedOut.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.jgl.TappedOut.dto.EventCreateDTO;
import com.jgl.TappedOut.dto.EventResponseDTO;
import com.jgl.TappedOut.dto.EventUpdateDTO;
import com.jgl.TappedOut.models.Event;

import jakarta.persistence.EntityNotFoundException;
/**
 * Mapper for converting between {@link Event} entities and DTOs.
 * Handles mapping including validations after it.
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Mapper(componentModel = "spring",
    uses = {SportMapper.class, UserMapper.class, MapperUtils.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class EventMapper {
    @Autowired
    protected MapperUtils mapperUtils;

    /**
     * Method to convert {@link EventCreateDTO} into a {@link Event}
     * 
     * @param dto the EventCreateDTO to convert from
     * @return the mapped Event entity to persist
     * @throws EntityNotFoundException if referenced sport or organizer not found
     * @throws IllegalArgumentException if date ranges are invalid
     */
    @Mapping(target = "sportId", expression = "java(mapperUtils.mapSport(dto.getSportId()))")
    @Mapping(target = "organizerId", expression = "java(mapperUtils.mapUser(dto.getOrganizerId()))")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract Event fromCreateDTO(EventCreateDTO dto);

    /**
     * Method to convert {@link Event} into a {@link EventResponseDTO}
     * 
     * @param event the Event to show
     * @return the mapped response DTO
     */
    @Mapping(target = "organizer", source = "organizerId")
    @Mapping(target = "sport", source = "sportId")
    @Mapping(target = "status", expression = "java(event.getStatus().toString())")
    public abstract EventResponseDTO toResponseDTO(Event event);

    /**
     * Method to convert {@link EventUpdateDTO} into a {@link Event}
     * 
     * @param dto the EventUpdateDTO to update from
     * @param event the Event entity to be updated
     * @throws IllegalArgumentException if date ranges are invalid
     * @throws EntityNotFoundException if referenced sport or organizer not found
     */
    @Mapping(target = "sportId", ignore = true)
    @Mapping(target = "organizerId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDTO(EventUpdateDTO dto, @MappingTarget Event event);


    /**
     * Validates event after mapping
     * 
     * @param event the model {@link Event}
     * @throws IllegalArgumentException if there's something invalid
     */
    @AfterMapping
    protected void validateEvent(@MappingTarget Event event){
        mapperUtils.validateDateRanges(event.getStartDate(), event.getEndDate());

        mapperUtils.validateRegistrationFee(event.getRegistrationFee());
    }
}