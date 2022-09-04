package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;


    @Override
    public Item findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    public List<Item> findAll(Long userId) {
        return itemRepository.findAll().stream().filter(item ->
                item.getOwner().getId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public Item create(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item update(Item updateItem) {
        Item oldItem = findItemById(updateItem.getId());
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

    public void patchItem(Item updateItem, Item oldItem) {
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


    // Реализуйте логику по добавлению нового комментария к вещи в сервисе
}
