package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return new ItemRequestDto(request.getId(), request.getDescription(), request.getRequestor(),
                request.getCreated(), ItemMapper.toItemsDto(request.getItems()));
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest(itemRequestDto.getId(), itemRequestDto.getDescription(),
                user, LocalDateTime.now().withNano(0), new ArrayList<>());

        if (itemRequestDto.getItems() != null) {
            List<Item> items = itemRequestDto.getItems().stream()
                    .map(itemDto -> ItemMapper.toItem(itemDto, user, itemRequest)).collect(Collectors.toList());
            itemRequest.setItems(items);
        }
        return itemRequest;
    }
}
