package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    ItemServiceImpl itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody Item item) {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") long userId,
                            @PathVariable("itemId") long id,
                            @RequestBody Item item) {
        return itemService.editItem(userId, id, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long id) {
        return itemService.getItem(id);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        return itemService.searchItems(text);
    }
}
