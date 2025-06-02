package com.jgl.TappedOut.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.jgl.TappedOut.dto.UserCreateDTO;
import com.jgl.TappedOut.dto.UserResponseDTO;
import com.jgl.TappedOut.dto.UserSecurityDTO;
import com.jgl.TappedOut.dto.UserUpdateDTO;
import com.jgl.TappedOut.models.User;
/**
 * Mapper for converting between {@link User} entities and DTOs.
 * Handles mapping including validations after it.
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Mapper(componentModel = "spring",
    uses = {UserTypeMapper.class, GenderMapper.class, MapperUtils.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserMapper {
    @Autowired
    protected MapperUtils mapperUtils;

    /**
     * Method to convert {@link UserCreateDTO} into a {@link User}
     * 
     * @param dto the UserCreateDTO to transform
     * @return the mapped User entity
     * @throws IllegalArgumentException if unique fields DNI and EMAIL already exist
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "typeId", expression = "java(mapperUtils.mapUserType(dto.getTypeId()))")
    @Mapping(target = "genderId", expression = "java(mapperUtils.mapGender(dto.getGenderId()))")
    public abstract User fromCreateDTO(UserCreateDTO dto);

    /**
     * Method to convert {@link User} into a {@link UserResponseDTO}
     * 
     * @param user the User to show
     * @return the response DTO
     */
    @Mapping(target = "type", source = "typeId")
    @Mapping(target = "genderId", source = "genderId")
    public abstract UserResponseDTO toResponseDTO(User user);

    /**
     * Method to update {@link User} from {@link UserUpdateDTO}
     * 
     * @param dto the UserUpdateDTO
     * @param entity the entity to update
     * @throws IllegalArgumentException if unique fields DNI and EMAIL already exist
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dni", ignore = true)
    @Mapping(target = "email", ignore = true)
    public abstract void updateFromDTO(UserUpdateDTO dto, @MappingTarget User entity);

    /**
     * Method to update {@link User} from {@link UserSecurityDTO}
     * 
     * @param dto the UserSecurityDTO
     * @param entity the entity to update
     * @throws IllegalArgumentException if unique fields DNI and EMAIL already exist
     */
    public void updateSecurity(UserSecurityDTO dto, @MappingTarget User entity) {
        if (!dto.getEmail().equalsIgnoreCase(entity.getEmail()))
            mapperUtils.validateUserUniqueFields(entity.getDni(), dto.getEmail());

        entity.setEmail(dto.getEmail());
        entity.setPasswordHash(mapperUtils.encodePassword(dto.getNewPassword()));
    }

    @AfterMapping
    protected void validateUser(UserCreateDTO dto, @MappingTarget User user) {
        mapperUtils.validateUserUniqueFields(dto.getDni(), dto.getEmail());
        user.setPasswordHash(mapperUtils.encodePassword(dto.getPassword()));
    }
}