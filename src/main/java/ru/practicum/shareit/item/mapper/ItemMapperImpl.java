package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapperImpl implements ItemMapper {

    private final UserService userService;
    private final ItemService itemService;
    private final CommentMapper commentMapper;

    private final ItemRequestServiceImpl itemRequestService;

    @Override
    public Item toItem(ItemDto itemDto, Long userId) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(userService.findUserById(userId))
                .itemRequest(itemRequestService.findItemRequestById(itemDto.getRequestId()))
                .build();
    }

    @Override
    public ItemDto toItemDto(Item item) {
        List<Comment> comments = itemService.findCommentByItem(item);
        List<CommentDto> commentsDto = commentMapper.toCommentDto(comments);


        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(getShortBookingDto(item.getLastBooking()))
                .nextBooking(getShortBookingDto(item.getNextBooking()))
                .comments(commentsDto)
                .requestId(item.getItemRequest() == null ? null : item.getItemRequest().getId())
                .build();
    }

    @Override
    public List<ItemDto> toItemDto(List<Item> items) {
        return items.stream().map(this::toItemDto).collect(Collectors.toList());
    }

    private ShortBookingDto getShortBookingDto(Booking booking) {
        if (booking == null) return null;
        return ShortBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
