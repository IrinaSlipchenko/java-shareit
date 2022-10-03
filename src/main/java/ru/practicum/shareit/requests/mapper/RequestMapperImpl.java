package ru.practicum.shareit.requests.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.dto.RequestOutputDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.requests.dto.RequestInputDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestMapperImpl implements RequestMapper {
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequest toItemRequest(RequestInputDto requestInputDto, Long userId) {
        return ItemRequest.builder()
                .description(requestInputDto.getDescription())
                .created(LocalDateTime.now())
                .requester(userService.findUserById(userId))
                .build();
    }

    @Override
    public RequestOutputDto toItemRequestDto(ItemRequest itemRequest) {
        return RequestOutputDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems() == null ? null :
                        itemMapper.toItemDto(itemRequest.getItems()))
                .build();
    }

    @Override
    public List<RequestOutputDto> toItemRequestDto(List<ItemRequest> itemRequests) {
        return itemRequests.stream().map(this::toItemRequestDto).collect(Collectors.toList());
    }


}
