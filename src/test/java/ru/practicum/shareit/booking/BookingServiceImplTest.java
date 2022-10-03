package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserService userService;

    @InjectMocks
    BookingServiceImpl bookingService;

    User user = User.builder()
            .id(1L)
            .build();

    User user2 = User.builder()
            .id(2L)
            .build();

    Item item = Item.builder()
            .available(true)
            .owner(user)
            .build();


    Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(1))
            .status(Status.APPROVED)
            .booker(user2)
            .item(item)
            .build();

    @Test
    void create() {
        when(bookingRepository.save(booking)).thenReturn(booking);
        var result = bookingService.create(booking);
        verify(bookingRepository, times(1)).save(booking);
        assertEquals(result, booking);
        assertSame(result.getStatus(), Status.WAITING);
    }

    @Test
    void findBookingById() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        var result = bookingService.findBookingById(booking.getId(), user2.getId());
        verify(bookingRepository, times(1)).findById(anyLong());
        assertEquals(result, booking);
    }

    @Test
    void approveBooking() {
    }

    @Test
    void allBooking() {
    }

    @Test
    void allBookingByOwner() {
    }
}