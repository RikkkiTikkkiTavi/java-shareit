package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class BookingMapper {
    public static BookingDto toItemDto(Item item) {
        return new BookingDto();
    }
}
