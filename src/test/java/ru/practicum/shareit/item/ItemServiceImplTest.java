package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;
    Item item;
    private Comment comment;
    private User author;

    @BeforeEach
    void setUp() {
        LocalDateTime created = LocalDateTime.now().withNano(0);

        item = Item.builder()
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

        author = User.builder()
                .id(1L)
                .name("Nina")
                .email("nina2009@yandex.ru")
                .bookings(Set.of(Booking.builder()
                        .id(3L)
                        .item(Item.builder()
                                .id(9L)
                                .build())
                        .status(Status.APPROVED)
                        .start(created.minusHours(1))
                        .build()))
                .build();

        comment = Comment.builder()
                .id(5L)
                .text("Замечательные санки")
                .author(author)
                .item(item)
                .created(created)
                .build();


    }

    @Test
    void create() {
        when(itemRepository.save(item)).thenReturn(item);
        var result = itemService.create(item);
        verify(itemRepository, times(1)).save(item);

        assertNotNull(result);
        assertEquals(item, result);
    }

    @Test
    void createComment_throws_whenNoPermissionToComment() {

        assertThrows(ValidationException.class, () -> itemService.createComment(comment));
    }

    @Test
    void findCommentByItem() {
        when(commentRepository.findAllByItem(item))
                .thenReturn(List.of());

        var result = itemService.findCommentByItem(item);
        verify(commentRepository, times(1)).findAllByItem(item);
        assertNotNull(result);
    }
}