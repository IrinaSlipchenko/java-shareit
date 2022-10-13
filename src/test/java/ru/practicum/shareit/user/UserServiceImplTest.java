package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Nina")
                .email("nina2009@yandex.ru")
                .build();
    }

    @Test
    void findUserById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        var result = userService.findUserById(anyLong());

        verify(userRepository, times(1)).findById(anyLong());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    void findUserByIdThrowException() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserById(anyLong()));
    }


    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        var result = userService.findAll();
        verify(userRepository, times(1)).findAll();

        assertEquals(1, result.size());
    }

    @Test
    void delete() {
        userService.delete(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void create() {
        when(userRepository.save(user)).thenReturn(user);
        var result = userService.create(user);
        verify(userRepository, times(1)).save(user);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void create_throws_whenNameIsNull() {
        User badUser = User.builder().build();
        assertThrows(ValidationException.class, () -> userService.create(badUser));
    }

    @Test
    void create_throws_whenSaveThrow() {
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(ConflictException.class, () -> userService.create(user));
    }

    @Test
    void update() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        var result = userService.update(user, user.getId());
        verify(userRepository, times(1)).save(user);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void update_throws_whenUpdateThrow() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(ConflictException.class, () -> userService.update(user, 1L));
    }
}