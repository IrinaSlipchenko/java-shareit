package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingOutputDto create(@RequestBody BookingInputDto bookingInputDto,
                                   @RequestHeader("X-Sharer-User-Id") Long bookerId) {

        Booking booking = bookingMapper.toBooking(bookingInputDto, bookerId);
        return bookingMapper.toBookingDto(bookingService.create(booking));
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto patchBooking(@PathVariable("bookingId") Long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam("approved") Boolean approved) {
        Booking booking = bookingService.approveBooking(bookingId, userId, approved);
        return bookingMapper.toBookingDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto findBooking(@PathVariable("bookingId") Long bookingId,
                                        @RequestHeader("X-Sharer-User-Id") Long userId) {

        Booking booking = bookingService.findBookingById(bookingId, userId);
        return bookingMapper.toBookingDto(booking);
    }

    @GetMapping
    public List<BookingOutputDto> allBooking(@RequestParam(defaultValue = "ALL") State state,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingMapper.toBookingDto(bookingService.allBooking(from, size, state, userId));
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> allBookingByOwner(@RequestParam(defaultValue = "ALL") State state,
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {

        return bookingMapper.toBookingDto(bookingService.allBookingByOwner(from, size, state, userId));
    }


}
