package com.jgl.TappedOut.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.jgl.TappedOut.dto.InscriptionCreateDTO;
import com.jgl.TappedOut.dto.InscriptionResponseDTO;
import com.jgl.TappedOut.dto.InscriptionUpdateDTO;
import com.jgl.TappedOut.models.Inscription;

import jakarta.persistence.EntityNotFoundException;

/**
 * Mapper for converting between {@link Inscription} entities and DTOs.
 * Handles mapping including validations after it.
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Mapper(componentModel  ="spring",
    uses = {UserMapper.class, EventMapper.class, CategoryMapper.class, MapperUtils.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class InscriptionMapper {
    @Autowired
    protected MapperUtils mapperUtils;

    /**
     * Method to convert {@link InscriptionCreateDTO} into a {@link Inscription}
     * 
     * @param dto the InscriptionCreateDTO to transform
     * @return the mapped Inscription entity
     * @throws EntityNotFoundException if referenced sport or organizer not found
     * @throws IllegalArgumentException if date ranges are invalid
     */
    @Mapping(target = "competitorId", expression = "java(mapperUtils.mapUser(dto.getCompetitorId()))")
    @Mapping(target = "event", expression = "java(mapperUtils.mapEvent(dto.getEventId()))")
    @Mapping(target = "categoryId", expression = "java(mapperUtils.mapCategory(dto.getCategoryId()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registerDate", ignore = true)
    public abstract Inscription fromCreateDTO(InscriptionCreateDTO dto);

    /**
     * Method to convert {@link Inscription} into a {@link InscriptionResponseDTO}
     * 
     * @param inscription the Inscription to show
     * @return the response DTO
     */
    @Mapping(target = "competitor", source = "competitorId")
    @Mapping(target = "event", source = "eventId")
    @Mapping(target = "category", source = "categoryId")
    public abstract InscriptionResponseDTO toResponseDTO(Inscription inscription);

    /**
     * Method to update {@link Inscription} from {@link InscriptionUpdateDTO}
     * 
     * @param dto the InscriptionUpdateDTO
     * @param entity the entity to update
     * @throws IllegalArgumentException if the category to make the inscription at does not belong to event
     * @throws EntityNotFoundException if referenced competitor, event or category not found
     */
    @Mapping(target = "competitorId", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "categoryId", expression = "java(mapperUtils.mapCategory(dto.getCategoryId()))")
    @Mapping(target = "registerDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDTO(InscriptionUpdateDTO dto, @MappingTarget Inscription entity);

    /**
     * Validates inscription after mapping
     * 
     * @param inscription the model {@link Inscription}
     * @throws IllegalArgumentException if the category is not at the event's sport
     * @throws IllegalStateException if the cancelling terms are invalid
     */
    @AfterMapping
    protected void validateInscriptionCreation(@MappingTarget Inscription inscription) {
        mapperUtils.validateCategoryEventMatch(inscription.getCategoryId(), inscription.getEvent());

        mapperUtils.validateInscription(inscription.getEvent().getStartDate());
    }

    /**
     * Validates inscription updating after mapping
     * 
     * @param dto the InscriptionUpdateDTO that's been mapped to flag when to validate (updateFromDTO)
     * @param inscription the model {@link Inscription}
     * @throws IllegalArgumentException if the category is not at the event's sport
     * @throws IllegalStateException if the cancelling terms are invalid
     */
    @AfterMapping
    protected void validateInscriptionUpdate(InscriptionUpdateDTO dto, @MappingTarget Inscription inscription) {
        mapperUtils.validateCategoryEventMatch(inscription.getCategoryId(), inscription.getEvent());

        mapperUtils.validateInscriptionCancellation(
            inscription.getRegisterDate(),
            inscription.getEvent().getStartDate(),
            dto.getPaymentStatus());

        mapperUtils.validateInscription(inscription.getEvent().getStartDate());
    }
}