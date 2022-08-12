package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserById(Long id);

    User create(User user);

    User update(User user);

    User update(User user, String email);

    boolean isEmailInUse(String email);

    void delete(Long id);

    List<User> findAll();
}
