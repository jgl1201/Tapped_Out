package com.jgl.TappedOut.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jgl.TappedOut.models.Event;
import com.jgl.TappedOut.models.Inscription;
import com.jgl.TappedOut.models.Result;
import com.jgl.TappedOut.models.User;
import com.jgl.TappedOut.repositories.EventRepository;
import com.jgl.TappedOut.repositories.InscriptionRepository;
import com.jgl.TappedOut.repositories.ResultRepository;
import com.jgl.TappedOut.repositories.UserRepository;

/**
 * Service class to handle permissions logic related with {@link User}
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Service
public class PermissionsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private InscriptionRepository inscriptionRepo;

    @Autowired
    private ResultRepository resultRepo;

    /**
     * Retrieves current authenticated user
     */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null)
            return null;

        return userRepo.findByEmail(auth.getName()).orElse(null);
    }

    /**
     * Verifies if current user has admin role
     */
    public boolean isAdmin() {
        return getCurrentUser() != null && "ADMIN".equals(getCurrentUser().getTypeId().getName());
    }

    /**
     * Verifies if current user has organizer role
     */
    public boolean isOrganizer() {
        return getCurrentUser() != null && "ORGANIZER".equals(getCurrentUser().getTypeId().getName());
    }

    /**
     * Verifies if current user has competitor role
     */
    public boolean isCompetitor() {
        return getCurrentUser() != null && "COMPETITOR".equals(getCurrentUser().getTypeId().getName());
    }

    /**
     * Verifies if user can edity the specified proffile
     * ADMIN: can edit any profile
     * ORGANIZER: can edit their own profile
     * COMPETITOR: can edit their own profile
     */
    public boolean canEditUser(User user) {
        User currUser = getCurrentUser();

        if (currUser == null) return false;

        if (isAdmin()) return true;

        return currUser.getId().equals(user.getId());
    }

    /**
     * Verifies if user can edit the specified event
     * ADMIN: can edit any event
     * ORGANIZER: can edit their events
     */
    public boolean canEditEvent(Long eventId) {
        User currUser = getCurrentUser();
        Event event = eventRepo.findById(eventId).orElse(null);

        if (currUser == null) return false;

        if (isAdmin()) return true;

        if (isOrganizer())
            return event != null && event.getOrganizerId().getId().equals(currUser.getId());

        return false;
    }

    /**
     * Verifies if user can see inscriptions of an specified event
     * ADMIN: can see inscriptions of any event
     * ORGANIZER: can see inscriptions of their events
     * COMPETITOR: can see their own inscriptions
     */
    public boolean canSeeInscriptions(Long eventId) {
        User currUser = getCurrentUser();
        Event event = eventRepo.findById(eventId).orElse(null);

        if (currUser == null) return false;

        if (isAdmin()) return true;

        if (isOrganizer())
            return event != null && event.getOrganizerId().getId().equals(currUser.getId());

        return false;
    }

    /**
     * Verifies if user can see inscriptions of a specified comepetitor
     * ADMIN: can see inscriptions of any competitor
     * COMPETITOR: can see their own inscriptions
     */
    public boolean canSeeCompetitorInscriptions(Long competitorId) {
        User currUser = getCurrentUser();

        if (currUser == null) return false;

        if (isAdmin()) return true;

        if (isCompetitor())
            return competitorId.equals(currUser.getId());

        return false;
    }

    /**
     * Verifies if user can edit a specified inscription
     * ADMIN: can edit any inscription
     * COMPETITOR: can edit their own inscriptions
     */
    public boolean canEditInscription(Long inscriptionId) {
        User currUser = getCurrentUser();
        Inscription inscription = inscriptionRepo.findById(inscriptionId).orElse(null);

        if (currUser == null) return false;

        if (isAdmin()) return true;

        if (isCompetitor())
            return inscription != null && inscription.getCompetitorId().getId().equals(currUser.getId());

        return false;
    }

    /**
     * Verifies if user can delete a specified inscription
     * ADMIN: can delete any inscription
     * COMPETITOR: can delete their own inscriptions
     */
    public boolean canDeleteInscription(Long inscriptionId) {
        return canEditInscription(inscriptionId);
    }

    /**
     * Verifies if user can create/edit results for a specified event
     * ADMIN: can create/edit results for any event
     * ORGANIZER: can create/edit results for their events
     */
    public boolean canEditResults(Long eventId) {
        User currUser = getCurrentUser();
        Event event = eventRepo.findById(eventId).orElse(null);

        if (currUser == null) return false;

        if (isAdmin()) return true;

        if (isOrganizer())
            return event != null && event.getOrganizerId().getId().equals(currUser.getId());

        return false;
    }

    /**
     * Verifies if user can edit a specified result
     * ADMIN: can edit any result
     * ORGANIZER: can edit results of their events
     */
    public boolean canEditResult(Long resultId) {
        User currUser = getCurrentUser();
        Result result = resultRepo.findById(resultId).orElse(null);

        if (result == null) return false;

        Event event = eventRepo.findById(result.getEventId().getId()).orElse(null);

        if (currUser == null) return false;

        if (isAdmin()) return true;

        if (isOrganizer())
            return result != null && event.getOrganizerId().getId().equals(currUser.getId());

        return false;
    }

    /**
     * Verifies if user can access to specific data of an event as organizer
     * (for endpoints that require being the organizer of the event)
     */
    public boolean isEventOrganizer(Long eventId) {
        User currUser = getCurrentUser();
        Event event = eventRepo.findById(eventId).orElse(null);

        if (currUser == null) return false;

        return event != null && event.getOrganizerId().getId().equals(currUser.getId());
    }
}