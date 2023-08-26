package ru.practicum.shareit.item.validator;


import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemValidatorTest {

    private ItemDto itemDto;

    @Test
    void itemAvailableMustBeNotNull() {
        itemDto = new ItemDto(1, "ItemOne", "DescOne", null, 0);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkItem(itemDto));
    }

    @Test
    void itemNameMustBeNotNull() {
        itemDto = new ItemDto(1, null, "DescOne", true, 0);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkItem(itemDto));
    }

    @Test
    void itemDescriptionMustBeNotNull() {
        itemDto = new ItemDto(1, "ItemOne", null, true, 0);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkItem(itemDto));
    }
}