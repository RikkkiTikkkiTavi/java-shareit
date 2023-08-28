package ru.practicum.shareit.item.validator;


import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemValidationException;

import java.time.LocalDateTime;

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

    @Test
    void commentTextMustBeNotEmpty() {
        CommentDto commentDto = new CommentDto(1, "author", "", LocalDateTime.now());
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkComment(commentDto));
    }
}