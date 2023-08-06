package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemBookingDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingResponseDto lastBooking;
    private BookingResponseDto nextBooking;
    private List<CommentDto> comments;
}

