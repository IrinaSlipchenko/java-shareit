package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * The class helps listen to user request at "/users"
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return userMapper.toUserDto(user);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User user = userService.create(userMapper.toUser(userDto));
        return userMapper.toUserDto(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable("id") Long id) {
        User user = userService.update(userMapper.toUser(userDto), id);
        return userMapper.toUserDto(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);

    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }
}

