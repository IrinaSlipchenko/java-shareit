package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * This class describes the item for rent.
 */
@Data
@Builder
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean availableStatus;
    private User owner; // или хранить id юзера - владельца ?
    private String request;
}
