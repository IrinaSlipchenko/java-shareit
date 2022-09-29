package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.OffsetLimitPageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl {
    private final ItemRequestRepository requestRepository;

    public ItemRequest findItemRequestById(Long id) {
        if (id == null) return null;
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found"));
    }

    public ItemRequest create(ItemRequest itemRequest) {
        return requestRepository.save(itemRequest);
    }

    public List<ItemRequest> findAllItemRequest(Long userId) {
        return requestRepository.findAllByRequesterId(userId);
    }

    public List<ItemRequest> findPage(Long userId, String from, String size) {

        Pageable pageable = OffsetLimitPageable.of(
                from != null ? Integer.parseInt(from) : 0,
                size != null ? Integer.parseInt(size) : 10,
                Sort.by(Sort.Direction.DESC, "created"));

        return requestRepository.findAllByRequesterIdNot(userId, pageable);

    }
}
