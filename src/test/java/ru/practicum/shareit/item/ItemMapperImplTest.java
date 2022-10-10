package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemMapperImplTest {
    @Mock
    private UserService userService;
    @Mock
    private ItemRequestServiceImpl itemRequestService;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemMapperImpl itemMapper;

    Item item = Item.builder()
            .id(2L)
            .name("Санки")
            .description("Санки детские с ручкой")
            .available(true)
            .owner(User.builder()
                    .id(22L)
                    .name("Kristina")
                    .email("Kris1990@gmail.com")
                    .build())
            .lastBooking(Booking.builder()
                    .id(77L)
                    .booker(User.builder()
                            .id(88L)
                            .build())
                    .build())
            .nextBooking(Booking.builder()
                    .id(101L)
                    .booker(User.builder()
                            .id(99L)
                            .build())
                    .build())
            .itemRequest(ItemRequest.builder()
                    .id(125L)
                    .build())
            .build();

    CommentDto commentDto1 = CommentDto.builder()
            .id(5L)
            .text("Очень нужная вещь")
            .authorName("Tom")
            .created(LocalDateTime.now())
            .build();

    CommentDto commentDto2 = CommentDto.builder()
            .id(6L)
            .text("Тестовый коммент")
            .authorName("James")
            .created(LocalDateTime.now().plusHours(1))
            .build();

    ItemDto itemDto = ItemDto.builder()
            .name("Санки")
            .requestId(3L)
            .build();

    User owner = User.builder()
            .id(1L)
            .name("Nina")
            .email("nina2009@yandex.ru")
            .build();

    ItemRequest itemRequest = ItemRequest.builder()
            .id(3L)
            .build();
    @Test
    void toItem() {
        when(userService.findUserById(anyLong())).thenReturn(owner);
        when(itemRequestService.findItemRequestById(itemDto.getRequestId()))
                .thenReturn(itemRequest);

        var result = itemMapper.toItem(itemDto, anyLong());
        assertEquals(itemDto.getName(), result.getName());
    }

    @Test
    void toItemDto() {
        when(itemService.findCommentByItem(item)).thenReturn(new ArrayList<>());
        when(commentMapper.toCommentDto(anyList()))
                .thenReturn(List.of(commentDto1, commentDto2));

        var result = itemMapper.toItemDto(item);

        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(ShortBookingDto.builder()
                .id(77L).bookerId(88L).build(), result.getLastBooking());
        assertEquals(List.of(commentDto1, commentDto2), result.getComments());
    }

}