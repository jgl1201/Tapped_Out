package com.jgl.TappedOut.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Represents the {@link Category} for a {@link Event}
 * Is the relationship between events and categories
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Data
public class EventCategoryId implements Serializable {
    private Long eventId;
    private Long categoryId;
}