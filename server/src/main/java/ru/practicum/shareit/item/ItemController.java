package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody ItemDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") long userId,
                            @PathVariable("itemId") long itemId,
                            @RequestBody ItemDto item) {
        return itemService.editItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemBookingDto getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long id) {
        return itemService.getItem(userId, id);
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
