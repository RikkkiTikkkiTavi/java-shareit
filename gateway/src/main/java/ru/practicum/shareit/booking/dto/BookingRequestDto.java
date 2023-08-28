package ru.practicum.shareit.booking.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BookingRequestDto {
	long itemId;

	LocalDateTime start;

	LocalDateTime end;
}
