package ru.practicum.shareit.booking.validator;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.exception.BookingValidateException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingValidatorTest {

    private BookingRequestDto brd;

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
}