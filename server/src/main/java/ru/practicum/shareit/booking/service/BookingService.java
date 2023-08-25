package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto addBooking(long itemId, BookingRequestDto booking);

    BookingResponseDto getBooking(long userId, long bookingId);

    BookingResponseDto approveBooking(long userId, long bookingId, boolean approved);

    List<BookingResponseDto> getBookingsByBooker(long userId, String state, int from, int size);

    List<BookingResponseDto> getBookingsByOwner(long ownerId, String state, int from, int size);
}
