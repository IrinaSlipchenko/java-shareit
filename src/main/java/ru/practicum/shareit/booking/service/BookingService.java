package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    Booking create(Booking booking);

    Booking findBookingById(Long bookingId, Long userId);

    Booking approveBooking(Long bookingId, Long userId, Boolean approved);

    List<Booking> allBooking(Integer from, Integer size, State state, Long userId);

    List<Booking> allBookingByOwner(Integer from, Integer size, State state, Long userId);
}
