package com.jgl.TappedOut.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.jgl.TappedOut.dto.ResultCreateDTO;
import com.jgl.TappedOut.dto.ResultResponseDTO;
import com.jgl.TappedOut.dto.ResultUpdateDTO;
import com.jgl.TappedOut.models.Result;

/**
 * Mapper for converting between {@link Result} entities and DTOs.
 * Handles mapping including validations after it.
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Mapper(componentModel = "spring",
    uses = {EventMapper.class, CategoryMapper.class, UserMapper.class, MapperUtils.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ResultMapper {
    @Autowired
    protected MapperUtils mapperUtils;

    /**
     * Method to convert {@link ResultCreateDTO} into a {@link Result}
     * 
     * @param dto the ResultCreateDTO to convert from
     * @return the mapped Result entity to persist
     * @throws IllegalStateException if registering before event starts
     * @throws IllegalArgumentException if invalid position or user not registered at event/category
     */
    @Mapping(target = "eventId", expression = "java(mapperUtils.mapEvent(dto.getEventId()))")
    @Mapping(target = "categoryId", expression = "java(mapperUtils.mapCategory(dto.getCategoryId()))")
    @Mapping(target = "competitorId", expression = "java(mapperUtils.mapUser(dto.getCompetitorId()))")
    @Mapping(target = "id", ignore = true)
    public abstract Result fromCreateDTO(ResultCreateDTO dto);

    /**
     * Method to convert {@link Result} into a {@link ResultResponseDTO}
     * 
     * @param result the Result to show
     * @return the mapped response DTO
     */
    @Mapping(target = "event", source = "eventId")
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "competitor", source = "competitorId")
    public abstract ResultResponseDTO toResponseDTO(Result result);

    /**
     * Method to covert {@link ResultUpdateDTO} into a {@link Result}
     * 
     * @param dto the ResultUpdateDTO to update from
     * @param result the Result entity to be updated
     * @throws IllegalStateException if registering before event starts
     * @throws IllegalArgumentException if invalid position or user not registered at event/category
     */
    @Mapping(target = "eventId", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "competitorId", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDTO(ResultUpdateDTO dto, @MappingTarget Result result);

    /**
     * Validates result after mapping
     * 
     * @param result the model {@link Result}
     * @throws IllegalStateException registering result before event0s start
     * @throws IllegalArgumentException if invalid position or not user at that category / event
     */
    @AfterMapping
    protected void validateResult(@MappingTarget Result result) {
        mapperUtils.validatePosition(result.getPosition());

        mapperUtils.validateResultRegister(
            result.getCompetitorId().getId(),
            result.getEventId().getId(),
            result.getCategoryId().getId()
        );

        mapperUtils.validateResultTiming(result.getEventId().getStartDate());
    }
}