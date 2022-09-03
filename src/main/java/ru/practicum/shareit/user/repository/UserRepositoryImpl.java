package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private long id;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        if (isEmailInUse(user.getEmail())) {
            throw new ConflictException("email already exist");
        }
        user.setId(nextId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user, String oldEmail) {
        users.put(user.getId(), user);
        emails.remove(oldEmail);
        emails.add(user.getEmail());
        return user;
    }

    public boolean isEmailInUse(String email) {
        return emails.contains(email);
    }

    @Override
    public void delete(Long id) {
        if (users.containsKey(id)) {
            emails.remove(users.get(id).getEmail());
            users.remove(id);
        } else {
            throw new NotFoundException("user not found");
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private long nextId() {
        return ++id;
    }
}
