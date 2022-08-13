package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    private long id;

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item create(Item item) {
        item.setId(nextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item oldItem = items.get(item.getId());
        if (oldItem == null) {
            throw new NotFoundException("item not found");
        }
        if (!oldItem.getOwnerId().equals(item.getOwnerId())) {
            throw new NotFoundException("wrong user");
        }
        Item newItem = Item.builder()
                .id(oldItem.getId())
                .name(item.getName() == null ? oldItem.getName() : item.getName())
                .description(item.getDescription() == null ? oldItem.getDescription() : item.getDescription())
                .available(item.getAvailable() == null ? oldItem.getAvailable() : item.getAvailable())
                .ownerId(oldItem.getOwnerId())
                .build();
        items.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    private long nextId() {
        return ++id;
    }
}
