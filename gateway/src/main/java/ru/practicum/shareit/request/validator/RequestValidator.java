package ru.practicum.shareit.request.validator;

import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public class RequestValidator {
    public static void checkDescription(ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isEmpty()) {
            throw new ItemValidationException("Текст запроса не может быть пустым");
        }
    }

    public static void checkFromAndSize(long from, long size) {
        if (from < 0 || size <= 0) {
            throw new ItemValidationException("Размер не может быть отрицательным");
        }
    }
}
