package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingOutputDto create(@Valid @RequestBody BookingInputDto bookingInputDto,
                                   @RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId) {

        Booking booking = bookingMapper.toBooking(bookingInputDto, bookerId);
        return bookingMapper.toBookingDto(bookingService.create(booking));
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto patchBooking(@PathVariable("bookingId") Long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                         @RequestParam("approved") Boolean approved) {
        Booking booking = bookingService.approveBooking(bookingId, userId, approved);
        return bookingMapper.toBookingDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto findBooking(@PathVariable("bookingId") Long bookingId,
                                        @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {

        Booking booking = bookingService.findBookingById(bookingId, userId);
        return bookingMapper.toBookingDto(booking);
    }

    @GetMapping
    public List<BookingOutputDto> allBooking(@RequestParam(defaultValue = "ALL") State state,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return bookingMapper.toBookingDto(bookingService.allBooking(state, userId));
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> allBookingByOwner(@RequestParam(defaultValue = "ALL") State state,
                                                    @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {

        return bookingMapper.toBookingDto(bookingService.allBookingByOwner(state, userId));
    }


}
