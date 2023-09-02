package ru.practicum.shareit.request.validator;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestValidatorTest {

    @Test
    void checkDescription() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "", null, null, null);
        assertThrows(ItemValidationException.class, () -> RequestValidator.checkDescription(itemRequestDto));
    }

    @Test
    void checkFromAndSize() {
        assertThrows(ItemValidationException.class, () -> RequestValidator.checkFromAndSize(-1, 1));
        assertThrows(ItemValidationException.class, () -> RequestValidator.checkFromAndSize(0, 0));
    }
}