package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;


    @Override
    public Item findItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    public List<Item> findAll(Long userId) {
        return itemRepository.findAll().stream().filter(item ->
                item.getOwner().getId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public Item create(Item item) {
        validateUser(item.getOwner().getId());
        return itemRepository.save(item);
    }

    @Override
    public Item update(Item item) {
        validateUser(item.getOwner().getId());
        return itemRepository.save(item);
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

    private void validateUser(Long id) {
        userService.findUserById(id);
    }
}
