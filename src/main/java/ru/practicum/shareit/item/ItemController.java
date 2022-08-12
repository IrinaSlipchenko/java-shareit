package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        return null;
    }

    /*
    Редактировать вещь может только её владелец.
    Изменить можно название, описание и статус доступа.
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable Long itemId) {
        return null;
    }

    @GetMapping("/{itemId}")
    public ItemDto findItem(@PathVariable("itemId") Long itemId){
        Item item = itemService.findItemById(itemId);
        return ItemMapper.toItemDto(item);
    }

}
