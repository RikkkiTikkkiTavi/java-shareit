package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    BookingService bookingService;
    @PostMapping
    public BookingDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody Item item) {
        return null;
    }
}
