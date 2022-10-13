package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.dto.RequestInputDto;
import ru.practicum.shareit.requests.mapper.RequestMapperImpl;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestMapperImplTest {
    @Mock
    private UserService userService;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private RequestMapperImpl requestMapper;

    private User requester;
    private RequestInputDto requestInputDto;
    private ItemRequest itemRequest;
    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private LocalDateTime created = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        Item item1 = Item.builder()
                .id(55L)
                .build();
        Item item2 = Item.builder()
                .id(77L)
                .build();

        itemDto1 = ItemDto.builder()
                .id(55L)
                .build();
        itemDto2 = ItemDto.builder()
                .id(77L)
                .build();

        requester = User.builder()
                .id(11L)
                .name("Donna")
                .email("donna2003@yandex.ru")
                .build();

        requestInputDto = new RequestInputDto("Мне нужен ковер-самолет," +
                "может есть у кого ?");

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Мне нужен ковер-самолет, может есть у кого ?")
                .created(created)
                .items(List.of(item1, item2))
                .build();
    }

    @Test
    void toItemRequest() {
        when(userService.findUserById(anyLong())).thenReturn(requester);

        ItemRequest testItemRequest = requestMapper.toItemRequest(requestInputDto, requester.getId());

        assertEquals(requestInputDto.getDescription(), testItemRequest.getDescription());
        assertEquals(requester, testItemRequest.getRequester());
    }

    @Test
    void toItemRequestDto() {
        when(itemMapper.toItemDto(itemRequest.getItems()))
                .thenReturn(List.of(itemDto1, itemDto2));

        var result = requestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(created, result.getCreated());
        assertEquals(List.of(itemDto1, itemDto2), result.getItems());
    }

}