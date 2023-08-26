package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestMapperTest {

    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;

    private User requestor;

    @BeforeEach
    void setUp() {
        requestor = new User();
        itemRequestDto = new ItemRequestDto(1, "desc", requestor, LocalDateTime.now().withNano(0), new ArrayList<>());
        itemRequest = new ItemRequest(1, "desc", requestor, LocalDateTime.now().withNano(0), new ArrayList<>());
    }

    @Test
    void toItemRequestDto() {
        assertEquals(itemRequestDto, ItemRequestMapper.toItemRequestDto(itemRequest));
    }

    @Test
    void toItemRequest() {
        assertEquals(itemRequest, ItemRequestMapper.toItemRequest(itemRequestDto, requestor));
    }
}