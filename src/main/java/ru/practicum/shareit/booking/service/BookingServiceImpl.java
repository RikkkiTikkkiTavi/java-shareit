package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BookingServiceImpl {
    private final BookingRepository bookingRepository;

    @Transactional
    public BookingDto addBooking(long userId, Item item) {
        return null;
    }

}
