package com.jgl.TappedOut.service;

import java.util.List;

import com.jgl.TappedOut.dto.SportCreateDTO;
import com.jgl.TappedOut.dto.SportResponseDTO;

/**
 * Interface to declare methods needed at {@link SportServiceImpl}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public interface SportService {
    List<SportResponseDTO> getAllSports();
    SportResponseDTO getSportById(Long id);
    SportResponseDTO getSportByName(String name);
    SportResponseDTO createSport(SportCreateDTO dto);
    SportResponseDTO updateSport(Long id, SportCreateDTO dto);
    void deleteSport(Long id);
}