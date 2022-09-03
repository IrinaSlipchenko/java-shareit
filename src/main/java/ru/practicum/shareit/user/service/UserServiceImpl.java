package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
                .orElseThrow(()-> new NotFoundException("User not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getEmail() == null) {
            throw new ValidationException("invalid fields");
        }

        try {
            return userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new ConflictException("email already in use");
        }
    }

    @Override
    public User update(User updateUser, Long id) {
        User oldUser = findUserById(id);
        patchUser(updateUser, oldUser);  // mutate old user
        try {
            return userRepository.save(oldUser);
        }catch (DataIntegrityViolationException e){
            throw new ConflictException("email already in use");
        }
    }

    private void patchUser(User updateUser, User oldUser) {
        if (updateUser.getName() != null) {
            oldUser.setName(updateUser.getName());
        }
        if (updateUser.getEmail() != null) {
            oldUser.setEmail(updateUser.getEmail());
        }
    }

}
