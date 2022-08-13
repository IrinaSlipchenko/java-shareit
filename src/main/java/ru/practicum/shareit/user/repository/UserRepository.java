package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.BaseRepository;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends BaseRepository<User> {

    User update(User user, String email);

    boolean isEmailInUse(String email);

}
