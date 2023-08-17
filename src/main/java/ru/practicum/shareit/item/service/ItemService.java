package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(long id, ItemDto item);

    ItemDto editItem(long userId, long itemId, ItemDto item);

    ItemBookingDto getItem(long userId, long itemId);

    List<ItemBookingDto> getUserItems(long userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(long userId, long itemId, CommentDto comment);
}
