package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    BookingRepository bookingRepository;
    @InjectMocks
    BookingServiceImpl bookingService;

    public static final Long NO_SUCH_USER_EXISTS = 137L;

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

    Item badItem = Item.builder()
            .available(false)
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

    Booking testBooking = Booking.builder()
            .id(21L)
            .start(LocalDateTime.now().plusHours(3))
            .end(LocalDateTime.now().plusDays(2))
            .status(Status.WAITING)
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
    void create_throws_whenItemIsUnavailable() {
        Booking badBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.REJECTED)
                .booker(user2)
                .item(badItem)
                .build();

        assertThrows(ValidationException.class, () -> bookingService.create(badBooking));
    }

    @Test
    void create_throws_whenWrongBooker() {
        Booking badBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.REJECTED)
                .booker(user)
                .item(item)
                .build();

        assertThrows(NotFoundException.class, () -> bookingService.create(badBooking));
    }

    @Test
    void create_throws_whenWrongDate() {
        Booking badBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now())
                .status(Status.REJECTED)
                .booker(user2)
                .item(item)
                .build();

        assertThrows(ValidationException.class, () -> bookingService.create(badBooking));
    }

    @Test
    void findBookingById() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        var result = bookingService.findBookingById(
                booking.getItem().getOwner().getId(), user2.getId());

        verify(bookingRepository, times(1)).findById(anyLong());
        assertEquals(result, booking);
    }

    @Test
    void findBookingById_throws_whenWrongUser() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService
                .findBookingById(booking.getBooker().getId(), NO_SUCH_USER_EXISTS));
    }

    @Test
    void approveBookingApproved() {
        when(bookingRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(testBooking));
        var result = bookingService.approveBooking(
                testBooking.getId(), testBooking.getItem().getOwner().getId(), true);

        assertNotNull(result);
        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    void approveBookingREJECTED() {
        when(bookingRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(testBooking));
        var result = bookingService.approveBooking(
                testBooking.getId(), testBooking.getItem().getOwner().getId(), false);

        assertNotNull(result);
        assertEquals(Status.REJECTED, result.getStatus());
    }


    @Test
    void approveBooking_throws_whenWrongUser() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(testBooking));

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(
                1L, 2L, true));
    }

    @Test
    void approveBooking_throws_whenWrongStatus() {
        Booking badBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.REJECTED)
                .booker(user)
                .item(item)
                .build();

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(badBooking));

        assertThrows(ValidationException.class, () -> bookingService.approveBooking(
                1L, 1L, true));
    }

}