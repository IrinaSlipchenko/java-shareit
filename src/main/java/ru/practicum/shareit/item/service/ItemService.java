package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item findItemById(Long itemId);

    Item create(Item item);

    List<Item> findAll(Long id);

    Item update(Item item);

    List<Item> searchByKeyword(String text);
}
