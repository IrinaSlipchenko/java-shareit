package ru.practicum.shareit;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    Optional<T> findById(Long id);

    T create(T entity);

    T update(T entity);

    void delete(Long id);

    List<T> findAll();
}
