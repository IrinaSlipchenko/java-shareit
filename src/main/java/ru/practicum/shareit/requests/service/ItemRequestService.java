package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest findItemRequestById(Long id);

    ItemRequest create(ItemRequest itemRequest);

    List<ItemRequest> findAllItemRequest(Long userId);

    List<ItemRequest> findPage(Long userId, Integer from, Integer size);
}
