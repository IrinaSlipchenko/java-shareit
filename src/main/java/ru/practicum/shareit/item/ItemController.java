package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                    @PathVariable Long itemId) {

        Comment comment = commentMapper.toComment(commentDto, userId, itemId);
        return commentMapper.toCommentDto(itemService.createComment(comment));
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        Item item = itemMapper.toItem(itemDto, userId);
        return itemMapper.toItemDto(itemService.create(item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                          @PathVariable Long itemId) {

        Item item = itemMapper.toItem(itemDto, userId);
        item.setId(itemId);
        return itemMapper.toItemDto(itemService.update(item));
    }

    @GetMapping("/{itemId}")
    public ItemDto findItem(@PathVariable("itemId") Long itemId) {
        Item item = itemService.findItemById(itemId);
        return itemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemMapper.toItemDto(itemService.findAll(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        return itemMapper.toItemDto(itemService.searchByKeyword(text));
    }

}
