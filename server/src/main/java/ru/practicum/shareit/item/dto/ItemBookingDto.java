package ru.practicum.shareit.item.dto;

import lombok.Value;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Value
public class ItemBookingDto {
    long id;
    String name;
    String description;
    Boolean available;
    BookingResponseDto lastBooking;
    BookingResponseDto nextBooking;
    List<CommentDto> comments;
}

