package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Item findItemById(Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (Objects.equals(item.getOwner().getId(), userId)) {
            List<Booking> bookings = bookingRepository.findAllByItem(item);
            item.setLastBooking(getLastBooking(bookings));
            item.setNextBooking(getNextBooking(bookings));
        }
        return item;
    }

    @Override
    public List<Item> findAll(Long userId) {

        return itemRepository.findAllByOwnerId(userId).stream()
                .peek(item -> {
                    List<Booking> bookings = bookingRepository.findAllByItem(item);
                    item.setLastBooking(getLastBooking(bookings));
                    item.setNextBooking(getNextBooking(bookings));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Item create(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item update(Item updateItem) {
        Item oldItem = findItemById(updateItem.getId(), updateItem.getOwner().getId());
        if (!oldItem.getOwner().equals(updateItem.getOwner())) {
            throw new NotFoundException("wrong owner");
        }
        patchItem(updateItem, oldItem);  // mutate old Item
        return itemRepository.save(oldItem);
    }

    @Override
    public List<Item> searchByKeyword(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        String keyword = text.toLowerCase();
        return itemRepository.findAll().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(keyword)
                        || item.getDescription().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public Comment createComment(Comment comment) {
        User user = comment.getAuthor();
        Item item = comment.getItem();
        user.getBookings().stream().filter(booking ->
                        Objects.equals(booking.getItem(), item) &&
                                Objects.equals(booking.getStatus(), Status.APPROVED) &&
                                booking.getStart().isBefore(LocalDateTime.now()))
                .findFirst().orElseThrow(() -> new ValidationException("Only bookmakers can create comments"));
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findCommentByItem(Item item) {
        return commentRepository.findAllByItem(item);
    }

    private void patchItem(Item updateItem, Item oldItem) {
        if (updateItem.getName() != null) {
            oldItem.setName(updateItem.getName());
        }
        if (updateItem.getDescription() != null) {
            oldItem.setDescription(updateItem.getDescription());
        }
        if (updateItem.getAvailable() != null) {
            oldItem.setAvailable(updateItem.getAvailable());
        }
    }

    private Booking getNextBooking(List<Booking> bookings) {
        LocalDateTime current = LocalDateTime.now();

        return bookings.stream()
                .sorted()
                .filter(booking -> booking.getStart().isAfter(current))
                .findFirst().orElse(null);
    }

    private Booking getLastBooking(List<Booking> bookings) {
        LocalDateTime current = LocalDateTime.now();

        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getEnd).reversed())
                .filter(booking -> booking.getEnd().isBefore(current))
                .findFirst().orElse(null);

    }
}
