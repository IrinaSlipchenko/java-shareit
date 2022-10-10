package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingController controller;

    @Test
    void create() {
    }

    @Test
    void patchBooking() {
    }

    @Test
    void findBooking() {
    }

    @Test
    void allBooking() {
    }

    @Test
    void allBookingByOwner() {
    }
}