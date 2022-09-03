package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * This class describes the item for rent.
 */
@Data
@Builder
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
}
