package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserMapperImplTest {
    private User user;
    private UserDto userDto;
    private final UserMapper userMapper = new UserMapperImpl();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Nina")
                .email("nina2009@yandex.ru")
                .build();

        userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Test
    public void toUserDto() {
        UserDto testUserDto = userMapper.toUserDto(user);

        assertEquals(testUserDto.getName(), userDto.getName());
        assertEquals(testUserDto.getEmail(), userDto.getEmail());
        assertEquals(testUserDto.getId(), userDto.getId());
    }

    @Test
    void toUser() {
        User testUser = userMapper.toUser(userDto);

        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getEmail(), user.getEmail());
    }
}