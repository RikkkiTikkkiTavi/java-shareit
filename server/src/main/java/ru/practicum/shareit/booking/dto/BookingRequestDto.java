package ru.practicum.shareit.booking.dto;

import com.sun.istack.NotNull;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BookingRequestDto {

    long itemId;

    LocalDateTime start;

    @NotNull
    LocalDateTime end;
}
