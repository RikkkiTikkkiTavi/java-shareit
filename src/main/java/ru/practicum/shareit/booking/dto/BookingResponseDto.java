package ru.practicum.shareit.booking.dto;

import lombok.Value;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;


@Value
public class BookingResponseDto {

    long id;

    long bookerId;

    LocalDateTime start;

    LocalDateTime end;

    ItemDto item;

    UserDto booker;

    Status status;
}
