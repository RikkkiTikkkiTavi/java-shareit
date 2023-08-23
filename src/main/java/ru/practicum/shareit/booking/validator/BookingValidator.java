package ru.practicum.shareit.booking.validator;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.exception.BookingValidateException;

import java.time.LocalDateTime;

public class BookingValidator {

    public static void checkTime(BookingRequestDto booking) {

        if (booking.getEnd() == null || booking.getStart() == null) {
            throw new BookingValidateException("Старт и конец не могут быть равны null");
        }

        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new BookingValidateException("Время завершения аренды не может быть в прошлом");
        }

        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BookingValidateException("Аренда не может завершиться раньше начала");
        }

        if (booking.getEnd().equals(booking.getStart())) {
            throw new BookingValidateException("Старт и завершение аренды не могут происходить в одно и то же время");
        }

        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingValidateException("Старт аренды должен быть в будущем");
        }
    }

    public static void checkFromAndSize(long from, long size) {
        if (from < 0 || size <= 0) {
            throw new BookingValidateException("Размер не может быть отрицательным");
        }
    }
}
