package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.RequestInputDto;
import ru.practicum.shareit.requests.dto.RequestOutputDto;
import ru.practicum.shareit.requests.mapper.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;
    private final RequestMapper requestMapper;

    private final UserService userService;

    @PostMapping
    public RequestOutputDto create(@RequestBody RequestInputDto requestInputDto,
                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequest itemRequest = requestMapper.toItemRequest(requestInputDto, userId);
        return requestMapper.toItemRequestDto(requestService.create(itemRequest));
    }

    @GetMapping("/{requestId}")
    public RequestOutputDto findRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long requestId) {
        User user = userService.findUserById(userId);  // user validation

        return requestMapper.toItemRequestDto(requestService.findItemRequestById(requestId));
    }

    @GetMapping
    public List<RequestOutputDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {

        return requestMapper.toItemRequestDto(requestService.findAllItemRequest(userId));
    }

    @GetMapping("/all")
    public List<RequestOutputDto> findPageOfThisSize(@RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {

        return requestMapper.toItemRequestDto(requestService.findPage(userId, from, size));
    }
}
