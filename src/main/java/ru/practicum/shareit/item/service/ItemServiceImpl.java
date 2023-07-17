package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto addNewItem(long userId, Item item) {
        ItemValidator.checkItem(item);
        ItemValidator.checkExistenceUser(userStorage.getUser(userId));
        item.setOwner(userStorage.getUser(userId));
        return ItemMapper.toItemDto(itemStorage.createItem(item));
    }

    public ItemDto editItem(long userId, long itemId, Item item) {

        Item oldItem = itemStorage.getItem(itemId);
        ItemValidator.checkOwner(oldItem, userId);
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }

        return ItemMapper.toItemDto(itemStorage.updateItem(oldItem));

    }

    public ItemDto getItem(long itemId) {
        ItemValidator.checkExistenceItem(itemStorage.getItem(itemId));
        return ItemMapper.toItemDto(itemStorage.getItem(itemId));
    }

    public List<ItemDto> getUserItems(long userId) {
        return itemStorage
                .getAll()
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String text) {
        List<ItemDto> items = new ArrayList<>();
        if (text.isEmpty()) {
            return items;
        }
        for (Item item : itemStorage.getAll()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()) {
                items.add(ItemMapper.toItemDto(item));
            }
        }
        return items;
    }
}
