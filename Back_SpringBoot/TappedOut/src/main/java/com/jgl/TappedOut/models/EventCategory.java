package com.jgl.TappedOut.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the many-to-may relationship between {@link Event} and {@link Category}
 * This entity links competitions (events) with their available participation categories
 *   (as theere may not be all catefories of the sport, for example, minors categories)
 * 
 * @author Jorge García López
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "event_categories")
@IdClass(EventCategoryId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCategory {
    @Id
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event eventId;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;
}