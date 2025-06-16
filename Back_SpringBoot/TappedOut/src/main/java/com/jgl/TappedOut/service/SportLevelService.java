package com.jgl.TappedOut.service;

import java.util.List;

import com.jgl.TappedOut.dto.SportLevelCreateDTO;
import com.jgl.TappedOut.dto.SportLevelResponseDTO;
import com.jgl.TappedOut.dto.SportLevelUpdateDTO;

/**
 * Interface to declare methods needed at {@link SportLevelServiceImpl}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public interface SportLevelService {
    List<SportLevelResponseDTO> getAllSportLevels();
    List<SportLevelResponseDTO> getSportLevelsBySport(Long sportId);
    SportLevelResponseDTO getSportLevelById(Long id);
    SportLevelResponseDTO getSportLevelBySportAndName(Long sportId, String name);
    SportLevelResponseDTO createSportLevel(SportLevelCreateDTO sportLevelCreateDTO);
    SportLevelResponseDTO updateSportLevel(Long id, SportLevelUpdateDTO sportLevelUpdateDTO);
    void deleteSportLevel(Long id);
}