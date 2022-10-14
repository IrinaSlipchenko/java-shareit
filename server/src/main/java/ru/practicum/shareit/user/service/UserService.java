package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User findUserById(Long id);

    List<User> findAll();

    void delete(Long id);

    User create(User user);

    User update(User updateUser, Long id);


}
