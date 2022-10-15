package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                @Valid @RequestBody RequestInputDto requestInputDto) {
        log.info("Creating Request {}", requestInputDto);
        return requestClient.createRequest(userId, requestInputDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Get request, Id={}", requestId);
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestAll(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Get requests of userId={}", userId);
        return requestClient.getRequestAll(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsPage(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get requests page={}, size={}, of userId={}", from, size, userId);
        return requestClient.getRequestsPage(userId, from, size);
    }
}
