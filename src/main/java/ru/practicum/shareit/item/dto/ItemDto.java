package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * // TODO .
 */
@Data
@Builder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean availableStatus;
    private User owner; // или хранить id юзера - владельца ?
    private String request;

}
