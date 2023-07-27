package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ItemDto addNewItem(long userId, Item item) {
        ItemValidator.checkItem(item);
        userRepository.findById(userId)
                .orElseThrow(() -> new ItemNotFoundException("Пользователь с данным id не зарегистрирован"));
        item.setOwnerId(userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto editItem(long userId, long itemId, Item item) {
        Item oldItem = itemRepository.findById(itemId).orElseThrow();
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
        return ItemMapper.toItemDto(itemRepository.save(oldItem));
    }

    public ItemDto getItem(long itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь c данным id не зарегистрирована")));
    }

    public List<ItemDto> getUserItems(long userId) {
         return itemRepository.findByOwnerId(userId)
                 .stream()
                 .map(ItemMapper::toItemDto)
                 .collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findByNameOrDescriptionContainingIgnoreCase(text,text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
