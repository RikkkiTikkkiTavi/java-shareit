package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.validator.ItemValidator;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestBody ItemDto itemDto) {
        ItemValidator.checkItem(itemDto);
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> editItem(@PathVariable("itemId") long itemId,
                                           @RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody ItemDto itemDto) {
        log.info("Edit item {} with itemId={}, userId={}", itemDto, itemId, userId);
        return itemClient.editItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable("itemId") long itemId) {
        log.info("Get item with itemId={}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get items by user with userId={}", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam String text) {
        log.info("Search items with text {} by userId={}", text, userId);
        return itemClient.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable("itemId") long itemId,
                                             @RequestBody CommentDto comment) {
        log.info("Add comment {} for itemId={} by userId={}", comment, itemId, userId);
        return itemClient.addComment(userId, itemId, comment);
    }
}
