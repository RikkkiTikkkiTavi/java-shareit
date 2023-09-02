package ru.practicum.shareit.request.service;

import ru.practicum.shareit.user.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {

    ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getOwnerRequests(long userId);

    List<ItemRequestDto> getNotOwnerRequests(long userId, int from, int size);

    ItemRequestDto findById(long userId, long requestId);
}
