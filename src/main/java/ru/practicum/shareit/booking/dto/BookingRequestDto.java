package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingRequestDto {

    private long itemId;

    private LocalDateTime start;

    @NotNull(message = "Время завершения бронирования не может быть null")
    private LocalDateTime end;
}
