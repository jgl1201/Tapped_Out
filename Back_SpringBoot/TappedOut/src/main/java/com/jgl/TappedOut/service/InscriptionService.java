package com.jgl.TappedOut.service;

import java.util.List;

import com.jgl.TappedOut.dto.InscriptionCreateDTO;
import com.jgl.TappedOut.dto.InscriptionResponseDTO;
import com.jgl.TappedOut.dto.InscriptionUpdateDTO;
import com.jgl.TappedOut.models.PaymentStatus;

/**
 * Interface to declare methods needed at {@link InscriptionServiceImpl}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public interface InscriptionService {
    List<InscriptionResponseDTO> getAllInscriptions();
    List<InscriptionResponseDTO> getInscriptionsByCompetitor(Long competitorId);
    List<InscriptionResponseDTO> getInscriptionsByEvent(Long eventId);
    List<InscriptionResponseDTO> getInscriptionsByCategory(Long eventId, Long categoryId);
    List<InscriptionResponseDTO> getInscriptionByPaymentStatus(PaymentStatus status);
    List<InscriptionResponseDTO> getPaidInscriptionsByEvent(Long eventId);
    Long countPaidInscriptionsByEvent(Long eventId);
    InscriptionResponseDTO getInscriptionById(Long id);
    List<InscriptionResponseDTO> getInscriptionByCompetitorAndEvent(Long competitorId, Long eventId);
    InscriptionResponseDTO createInscription(InscriptionCreateDTO dto);
    InscriptionResponseDTO updateInscription(Long id, InscriptionUpdateDTO dto);
    void deleteInscription(Long id);
}