package com.jgl.TappedOut.service;

import java.util.List;

import com.jgl.TappedOut.dto.UserCreateDTO;
import com.jgl.TappedOut.dto.UserResponseDTO;
import com.jgl.TappedOut.dto.UserSecurityDTO;
import com.jgl.TappedOut.dto.UserUpdateDTO;

/**
 * Interface to declare methods needed at {@link SportLevelServiceImpl}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public interface UserService {
    List<UserResponseDTO> getAllUsers();
    List<UserResponseDTO> getUsersByType(Long typeId);
    List<UserResponseDTO> getUsersByGender(Long genderId);
    List<UserResponseDTO> getUsersByLocation(String country, String city);
    List<UserResponseDTO> searchUsers(String query);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByDni(String dni);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO createUser(UserCreateDTO userCreateDTO);
    UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);
    UserResponseDTO updateUserSecurity(Long id, UserSecurityDTO userSecurityDTO);
    void deleteUser(Long id);
}