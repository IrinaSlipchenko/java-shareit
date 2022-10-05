package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingMapperImplTest {
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;

    @InjectMocks
    private BookingMapperImpl bookingMapper;
    private BookingInputDto bookingInputDto;
    private Booking booking;
    private Item item;
    private User booker;
    private ShortUserDto shortUserDto;
    private ShortItemDto shortItemDto;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        shortUserDto = new ShortUserDto(1L);
        shortItemDto = new ShortItemDto(2L, "Санки");

        booker = User.builder()
                .id(1L)
                .name("Nina")
                .email("nina2009@yandex.ru")
                .build();

        item = Item.builder()
                .id(2L)
                .name("Санки")
                .description("Санки детские с ручкой")
                .available(true)
                .build();

        bookingInputDto = BookingInputDto.builder()
                .itemId(2L)
                .start(start)
                .end(end)
                .build();

        booking = Booking.builder()
                .id(3L)
                .start(start)
                .end(end)
                .status(Status.APPROVED)
                .booker(booker)
                .item(item)
                .build();
    }

    @Test
    void toBooking() {
        when(itemService.findItemById(bookingInputDto.getItemId(), null))
                .thenReturn(item);

        when(userService.findUserById(booker.getId())).thenReturn(booker);

        Booking testBooking = bookingMapper.toBooking(bookingInputDto, booker.getId());

        assertEquals(bookingInputDto.getStart(), testBooking.getStart());
        assertEquals(bookingInputDto.getEnd(), testBooking.getEnd());
        assertEquals(item, testBooking.getItem());
        assertEquals(booker, testBooking.getBooker());

    }

    @Test
    void toBookingDto() {

        BookingOutputDto testBookingOutputDto = bookingMapper.toBookingDto(booking);

        assertEquals(booking.getId(), testBookingOutputDto.getId());
        assertEquals(booking.getStart(), testBookingOutputDto.getStart());
        assertEquals(booking.getEnd(), testBookingOutputDto.getEnd());
        assertEquals(booking.getStatus(), testBookingOutputDto.getStatus());
        assertEquals(shortUserDto, testBookingOutputDto.getBooker());
        assertEquals(shortItemDto, testBookingOutputDto.getItem());
    }

}