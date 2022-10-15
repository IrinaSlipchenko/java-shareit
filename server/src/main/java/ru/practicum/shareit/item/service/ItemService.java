package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item findItemById(Long itemId, Long userId);

    Item create(Item item);

    List<Item> findAll(Integer from, Integer size, Long id);

    Item update(Item item);

    List<Item> searchByKeyword(String text);

    Comment createComment(Comment comment);

    List<Comment> findCommentByItem(Item item);

}
