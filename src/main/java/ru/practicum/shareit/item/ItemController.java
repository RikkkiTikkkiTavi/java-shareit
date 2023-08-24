package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.validator.ItemValidator;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody ItemDto itemDto) {
        ItemValidator.checkItem(itemDto);
        return itemService.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@PathVariable("itemId") long itemId,
                            @RequestHeader("X-Sharer-User-Id") long userId,
                            @RequestBody ItemDto itemDto) {
        return itemService.editItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemBookingDto getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemBookingDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId,
                                 @RequestBody CommentDto comment) {
        return itemService.addComment(userId, itemId, comment);
    }
}
