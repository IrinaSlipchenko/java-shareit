package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

        assertThat(testUserDto.getId(), equalTo(userDto.getId()));
        assertThat(testUserDto.getName(), equalTo(userDto.getName()));
        assertThat(testUserDto.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void toUser() {
        User testUser = userMapper.toUser(userDto);

        assertThat(testUser.getName(), equalTo(user.getName()));
        assertThat(testUser.getEmail(), equalTo(user.getEmail()));
    }
}