package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemMapper {
    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDto(List<Item> items);

    Item toItem(ItemDto itemDto, Long id);
}
