package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.validator.RequestValidator;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestBody ItemRequestDto itemRequestDto) {
        RequestValidator.checkDescription(itemRequestDto);
        log.info("Creating itemRequest {}, userId={}", itemRequestDto, userId);
        return itemRequestClient.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get requests for userId={}", userId);
        return itemRequestClient.getOwnerRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getNotOwnerRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        RequestValidator.checkFromAndSize(from, size);
        log.info("Get requests for all, expect userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getNotOwnerRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable("requestId") long requestId) {
        log.info("Get request with requestId={} for userId={}", requestId, userId);
        return itemRequestClient.getRequest(userId, requestId);
    }
}
