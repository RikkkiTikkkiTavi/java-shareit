package ru.practicum.shareit.booking.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.exception.BookingValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingValidatorTest {

    BookingRequestDto brd;

    @BeforeEach
    void setUp() {
        Item item = new Item();
        item.setId(2);
        User owner = new User();
        owner.setId(3);
        item.setOwner(owner);
    }

    @Test
    void throwExceptionIfStartOrEndEqualNull() {
        brd = new BookingRequestDto(1, null, LocalDateTime.of(2030, 1, 1, 1, 1, 10));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
        brd = new BookingRequestDto(1, LocalDateTime.of(2030, 1, 1, 1, 1, 1), null);
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionEndTimeInPast() {
        brd = new BookingRequestDto(1, LocalDateTime.of(2030, 1, 1, 1, 1, 1), LocalDateTime.of(1900, 1, 1, 1, 1, 1));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionIfEndBeforeStart() {
        brd = new BookingRequestDto(1, LocalDateTime.of(2030, 1, 1, 1, 1, 1), LocalDateTime.of(2029, 1, 1, 1, 1, 1));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionIfEndEqualStart() {
        brd = new BookingRequestDto(1, LocalDateTime.of(2030, 1, 1, 1, 1, 1), LocalDateTime.of(2030, 1, 1, 1, 1, 1));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionIfStartInPast() {
        brd = new BookingRequestDto(1, LocalDateTime.of(2000, 1, 1, 1, 1, 1), LocalDateTime.of(2030, 1, 1, 1, 1, 1));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionIfPageHaveNegativeFromOrSize() {
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkFromAndSize(-1, 10));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkFromAndSize(0, 0));
    }
}