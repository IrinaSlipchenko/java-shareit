package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getEmail() == null) {
            throw new ValidationException("invalid fields");
        }
        return userRepository.create(user);
    }

    @Override
    public User update(User user, Long id) {
        User oldUser = findUserById(id);
        User newUser;
        if (user.getEmail() == null) {
            newUser = User.builder()
                    .id(oldUser.getId())
                    .name(user.getName())
                    .email(oldUser.getEmail())
                    .build();
            return userRepository.update(newUser);
        } else {
            if (userRepository.isEmailInUse(user.getEmail())) {
                throw new ConflictException("email already exist");
            }
            newUser = User.builder()
                    .id(oldUser.getId())
                    .name(user.getName() != null ? user.getName() : oldUser.getName())
                    .email(user.getEmail())
                    .build();
            return userRepository.update(newUser, oldUser.getEmail());
        }
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
