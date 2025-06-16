package com.jgl.TappedOut.service;

/**
 * Utility class for Services common methods
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
public class Utils {
    /**
     * Method to check if name has changed
     * 
     * @param oldName Old NAME
     * @param newName New NAME
     * @return boolean
     */
    public boolean hasNameChanged(String oldName, String newName) {
        return !oldName.trim().equalsIgnoreCase(newName.trim());
    }
}