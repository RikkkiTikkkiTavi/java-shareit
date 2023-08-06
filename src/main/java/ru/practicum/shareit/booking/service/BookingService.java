package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto addBooking(long itemId, BookingRequestDto booking);

    BookingResponseDto getBooking(long userId, long itemId);

    BookingResponseDto approveBooking(long userId, long bookingId, boolean approved);

    List<BookingResponseDto> getBookingsByBooker(long userId, String state);

    List<BookingResponseDto> getBookingsByOwner(long ownerId, String state);
}
