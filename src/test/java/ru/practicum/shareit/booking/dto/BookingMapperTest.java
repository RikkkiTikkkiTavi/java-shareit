package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    private Booking booking;
    private BookingResponseDto bookingResponseDto;
    private BookingRequestDto bookingRequestDto;
    private Item item;
    private User booker;

    @BeforeEach
    void setUp() {
        booker = new User(1, "Booker", "booker@mail.ru");
        User owner = new User(2, "Owner", "owner@mail.ru");
        item = new Item(1, owner, "itemOne", "descriptionOne", true, null);
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2001, 1, 1, 1, 1, 1);
        booking = new Booking(0, start, end, item, booker, Status.APPROVED);
        bookingResponseDto = new BookingResponseDto(0, 1, start, end, item, booker, Status.APPROVED);
        bookingRequestDto = new BookingRequestDto(1, start, end);
    }

    @Test
    void toBookingResponseDto() {
        assertEquals(bookingResponseDto, BookingMapper.toBookingResponseDto(booking));
    }

    @Test
    void toBooking() {
        assertEquals(booking, BookingMapper.toBooking(bookingRequestDto, item, booker, Status.APPROVED));
    }

    @Test
    void toBookingsRequestDto() {
        List<Booking> bookings = List.of(booking);
        List<BookingResponseDto> bookingResponses = List.of(bookingResponseDto);
        assertEquals(bookingResponses, BookingMapper.toBookingsResponseDto(bookings));
    }
}