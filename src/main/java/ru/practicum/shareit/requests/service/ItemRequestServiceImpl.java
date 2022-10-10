package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.OffsetLimitPageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserService userService;

    @Override
    public ItemRequest findItemRequestById(Long id) {
        if (id == null) return null;
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found"));
    }

    @Override
    public ItemRequest create(ItemRequest itemRequest) {
        return requestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> findAllItemRequest(Long userId) {
        User user = userService.findUserById(userId);  // user validation
        return requestRepository.findAllByRequesterId(userId);
    }

    @Override
    public List<ItemRequest> findPage(Long userId, Integer from, Integer size) {
        User user = userService.findUserById(userId);  // user validation

        Pageable pageable = OffsetLimitPageable.of(from, size,
                Sort.by(Sort.Direction.DESC, "created"));

        return requestRepository.findAllByRequesterIdNot(userId, pageable);

    }
}
