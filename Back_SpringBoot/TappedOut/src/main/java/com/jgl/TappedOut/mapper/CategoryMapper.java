package com.jgl.TappedOut.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.jgl.TappedOut.dto.CategoryCreateDTO;
import com.jgl.TappedOut.dto.CategoryResponseDTO;
import com.jgl.TappedOut.dto.CategoryUpdateDTO;
import com.jgl.TappedOut.models.Category;

/**
 * Mapper for converting between {@link Category} entities and DTOs.
 * Handles mapping including validations after it.
 * 
 * @author Jorge García López
 * @version 1.1
 * @since 2025
 */
@Mapper(componentModel = "spring",
    uses = {SportMapper.class, GenderMapper.class, SportLevelMapper.class, MapperUtils.class})
public abstract class CategoryMapper {
    @Autowired
    protected MapperUtils mapperUtils;

    /**
     * Method to convert {@link CategoryCreateDTO} into a {@link Category}
     * 
     * @param dto the CategoryCreateDTO to convert from
     * @return the mapped Category entity to persist
     * @throws EntityNotFoundException if referenced sport, gender or level not found
     * @throws IllegalArgumentException if age or weight ranges are invalid
     */
    @Mapping(target = "sportId", expression = "java(mapperUtils.mapSport(dto.getSportId()))")
    @Mapping(target = "genderId", expression = "java(mapperUtils.mapGender(dto.getGenderId()))")
    @Mapping(target = "levelId", expression = "java(mapperUtils.mapLevel(dto.getLevelId()))")
    public abstract Category fromCreateDTO(CategoryCreateDTO dto);

    /**
     * Method to convert {@link Category} into a {@link CategoryResponseDTO}
     * 
     * @param category the Category to show
     * @return the mapped response DTO
     */
    @Mapping(target = "sport", source = "sportId")
    @Mapping(target = "gender", source = "genderId")
    @Mapping(target = "level", source = "levelId")
    public abstract CategoryResponseDTO toResponseDTO(Category category);

    /**
     * Method to convert {@link CategoryUpdateDTO} into a {@link Category}
     * 
     * @param dto the CategoryUpdateDTO to update from
     * @param category the entity Category to be updated
     * @throws IllegalArgumentException if age or weight ranges are invalid
     * @throws EntityNotFoundException if referenced sport, gender or level not found
     */
    @Mapping(target = "sportId", ignore = true)
    @Mapping(target = "genderId", ignore = true)
    @Mapping(target = "levelId", expression = "java(mapperUtils.mapLevel(dto.getLevelId()))")
    public abstract void updateFromDTO(CategoryUpdateDTO dto, @MappingTarget Category category);

    /**
     * Validates age and weight ranges after mapping
     * 
     * @param category the model {@link Category}
     * @throws IllegalArgumentException if there's something invalid
     */
    @AfterMapping
    protected void validateRanges(@MappingTarget Category category) {
        mapperUtils.validateAgeRanges(category.getMinAge(), category.getMaxAge());
        mapperUtils.validateWeightRanges(category.getMinWeight(), category.getMaxWeight());
    }

    
}