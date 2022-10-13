package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.OffsetLimitPageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;

    @Override
    public Booking create(Booking booking) {
        Item item = booking.getItem();
        if (!booking.getStart().isBefore(booking.getEnd())) {
            throw new ValidationException("wrong date Start or End");
        }
        if (Objects.equals(item.getOwner(), booking.getBooker())) {
            throw new NotFoundException("wrong booker");
        }

        if (item.getAvailable()) {
            booking.setStatus(Status.WAITING);
            return bookingRepository.save(booking);
        }
        throw new ValidationException("item is unavailable");
    }

    @Override
    public Booking findBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking not found"));

        if (Objects.equals(booking.getBooker().getId(), userId) ||
                Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            return booking;
        }
        throw new NotFoundException("wrong user");
    }

    @Override
    public Booking approveBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking not found"));

        if (booking.getStatus() != Status.WAITING) {
            throw new ValidationException("booking already approved");
        }
        if (Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            throw new NotFoundException("wrong user");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> allBooking(Integer from, Integer size, State state, Long userId) {
        Pageable pageable = OffsetLimitPageable.of(from, size, Sort.by(Sort.Direction.DESC, "start"));

        User user = userService.findUserById(userId);

        List<Booking> bookings = bookingRepository.findAllByBooker(user, pageable);

        return getBookingsByState(state, bookings);
    }

    @Override
    public List<Booking> allBookingByOwner(Integer from, Integer size, State state, Long userId) {
        Pageable pageable = OffsetLimitPageable.of(from, size,
                Sort.by(Sort.Direction.DESC, "start"));

        User user = userService.findUserById(userId);

        Set<Item> items = user.getItems();

        List<Booking> bookings = bookingRepository.findAllByItemIn(items, pageable);

        return getBookingsByState(state, bookings);
    }

    private List<Booking> getBookingsByState(State state, List<Booking> bookings) {
        LocalDateTime currentTime = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookings;
            case CURRENT:
                return bookings.stream()
                        .filter(booking -> booking.getStart().isBefore(currentTime) &&
                                booking.getEnd().isAfter(currentTime))
                        .collect(Collectors.toList());
            case PAST:
                return bookings.stream()
                        .filter(booking -> booking.getEnd().isBefore(currentTime))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings.stream()
                        .filter(booking -> booking.getStart().isAfter(currentTime))
                        .collect(Collectors.toList());
            case WAITING:
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == Status.WAITING)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == Status.REJECTED)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

}
