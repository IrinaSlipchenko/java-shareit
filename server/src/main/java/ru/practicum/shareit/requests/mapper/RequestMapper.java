package ru.practicum.shareit.requests.mapper;

import ru.practicum.shareit.requests.dto.RequestInputDto;
import ru.practicum.shareit.requests.dto.RequestOutputDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface RequestMapper {
    ItemRequest toItemRequest(RequestInputDto requestInputDto, Long userId);

    RequestOutputDto toItemRequestDto(ItemRequest itemRequest);

    List<RequestOutputDto> toItemRequestDto(List<ItemRequest> itemRequests);
}
