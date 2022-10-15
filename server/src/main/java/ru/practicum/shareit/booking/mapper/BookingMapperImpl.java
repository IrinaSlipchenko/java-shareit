package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingMapperImpl implements BookingMapper {
    private final ItemService itemService;
    private final UserService userService;


    @Override
    public Booking toBooking(BookingInputDto bookingInputDto, Long bookerId) {

        return Booking.builder()
                .start(bookingInputDto.getStart())
                .end(bookingInputDto.getEnd())
                .item(itemService.findItemById(bookingInputDto.getItemId(), null))
                .booker(userService.findUserById(bookerId))
                .build();
    }

    @Override
    public BookingOutputDto toBookingDto(Booking booking) {
        return BookingOutputDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(new ShortUserDto(booking.getBooker().getId()))
                .item(new ShortItemDto(booking.getItem().getId(), booking.getItem().getName()))
                .build();
    }

    @Override
    public List<BookingOutputDto> toBookingDto(List<Booking> bookings) {
        return bookings.stream().map(this::toBookingDto).collect(Collectors.toList());
    }
}
