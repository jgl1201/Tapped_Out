package com.jgl.TappedOut.service;

import java.util.List;

import com.jgl.TappedOut.dto.UserTypeCreateDTO;
import com.jgl.TappedOut.dto.UserTypeResponseDTO;

/**
 * Interface to declare methods needed at {@link UserTypeServiceImpl}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public interface UserTypeService {
    List<UserTypeResponseDTO> getAllUserTypes();
    UserTypeResponseDTO getUserTypeById(Long id);
    UserTypeResponseDTO getUserTypeByName(String name);
    UserTypeResponseDTO createUserType(UserTypeCreateDTO dto);
    UserTypeResponseDTO updateUserType(Long id, UserTypeCreateDTO dto);
    void deleteUserType(Long id);
}