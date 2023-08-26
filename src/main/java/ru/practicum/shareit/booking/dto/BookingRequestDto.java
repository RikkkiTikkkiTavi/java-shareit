package ru.practicum.shareit.booking.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class BookingRequestDto {

    long itemId;

    LocalDateTime start;

    @NotNull(message = "Время завершения бронирования не может быть null") LocalDateTime end;
}
