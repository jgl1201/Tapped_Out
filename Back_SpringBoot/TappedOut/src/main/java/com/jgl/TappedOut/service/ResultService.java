package com.jgl.TappedOut.service;

import java.util.List;

import com.jgl.TappedOut.dto.ResultCreateDTO;
import com.jgl.TappedOut.dto.ResultResponseDTO;
import com.jgl.TappedOut.dto.ResultUpdateDTO;

/**
 * Interface to declare methods needed at {@link ResultServiceImpl}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public interface ResultService {
    List<ResultResponseDTO> getAllResults();
    List<ResultResponseDTO> getResultsByEvent(Long eventId);
    List<ResultResponseDTO> getResultsByEventAndCategory(Long eventId, Long categoryId);
    List<ResultResponseDTO> getResultsByEventAndCompetitor(Long eventId, Long competitorId);
    List<ResultResponseDTO> getResultsByCompetitor(Long competitorId);
    List<ResultResponseDTO> getResultsByEventAndPosition(Long eventId, Integer position);
    ResultResponseDTO getResultById(Long id);
    ResultResponseDTO getWinnerByEventAndCategory(Long eventId, Long categoryId);
    ResultResponseDTO createResult(ResultCreateDTO dto);
    ResultResponseDTO updateResult(Long id, ResultUpdateDTO dto);
    void deleteResult(Long id);
}