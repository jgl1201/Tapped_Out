package com.jgl.TappedOut.service;

import java.util.List;

import com.jgl.TappedOut.dto.GenderCreateDTO;
import com.jgl.TappedOut.dto.GenderResponseDTO;

/**
 * Interface to declare methods needed at {@link GenderServiceImpl}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public interface GenderService {
    List<GenderResponseDTO> getAllGenders();
    GenderResponseDTO getGenderById(Long id);
    GenderResponseDTO getGenderByName(String name);
    GenderResponseDTO createGender(GenderCreateDTO dto);
    GenderResponseDTO updateGender(Long id, GenderCreateDTO dto);
    void deleteGender(Long id);
}