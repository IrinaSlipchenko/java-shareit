package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingMapper {
    Booking toBooking(BookingInputDto bookingInputDto, Long bookerId);

    BookingOutputDto toBookingDto(Booking booking);

    List<BookingOutputDto> toBookingDto(List<Booking> bookings);
}
