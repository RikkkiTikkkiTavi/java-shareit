package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return new BookingResponseDto(booking.getId(), booking.getBooker().getId(), booking.getStart(),
                booking.getEnd(), booking.getItem(), booking.getBooker(), booking.getStatus());
    }

    public static Booking toBooking(BookingRequestDto bookingRequestDto, Item item, User booker, Status status) {
        return new Booking(0, bookingRequestDto.getStart(), bookingRequestDto.getEnd(), item, booker, status);
    }

    public static List<BookingResponseDto> toBookingsResponseDto(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toBookingResponseDto).collect(Collectors.toList());
    }
}
