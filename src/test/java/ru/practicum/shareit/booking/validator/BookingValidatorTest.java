package ru.practicum.shareit.booking.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.exception.BookingValidateException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

class BookingValidatorTest {
    private Booking booking;
    private BookingRequestDto brd;

    @BeforeEach
    void setUp() {
        Item item = new Item();
        item.setId(2);
        User owner = new User();
        owner.setId(3);
        item.setOwner(owner);
        User user = new User(1, "userOne", "email@One.com");
        booking = new Booking(1, LocalDateTime.of(2030, 1, 1, 1, 1, 1), LocalDateTime.of(2030, 1, 1, 1, 1, 10), item,
                user, Status.WAITING);
        brd = new BookingRequestDto(1, LocalDateTime.of(2030, 1, 1, 1, 1, 1), LocalDateTime.of(2030, 1, 1, 1, 1, 10));
    }

    @Test
    void throwExceptionIfStartOrEndEqualNull() {
        brd.setStart(null);
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
        brd.setStart(LocalDateTime.now());
        brd.setEnd(null);
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionEndTimeInPast() {
        brd.setEnd(LocalDateTime.of(1900,1,1,1,1,1));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionIfEndBeforeStart() {
        brd.setEnd(LocalDateTime.of(2029,1,1,1,1,1));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionIfEndEqualStart() {
       brd.setEnd(brd.getStart());
       assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionIfStartInPast() {
        brd.setStart(LocalDateTime.of(1900,1,1,1,1,1));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkTime(brd));
    }

    @Test
    void throwExceptionIfUserNotOwner() {
        assertThrows(UserNotFoundException.class, () -> BookingValidator.validateUserId(99, booking));
    }

    @Test
    void throwExceptionIfPageHaveNegativeFromOrSize() {
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkFromAndSize(-1, 10));
        assertThrows(BookingValidateException.class, () -> BookingValidator.checkFromAndSize(0, 0));
    }


}