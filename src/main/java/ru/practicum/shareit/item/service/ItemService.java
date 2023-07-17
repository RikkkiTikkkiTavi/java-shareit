package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(long id, Item item);

    ItemDto editItem(long userId, long itemId, Item item);

    ItemDto getItem(long itemId);

    List<ItemDto> getUserItems(long userId);

    List<ItemDto> searchItems(String text);
}
